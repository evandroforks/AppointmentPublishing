package com.appointment.publishing.test;

import com.appointment.publishing.AppointmentPublishingApplication;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppointmentPublishingApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
@Transactional // https://stackoverflow.com/questions/51036215/spring-h2-test-db-does-not-reset-before-each-test
public class NotificationControllerTest {

  private MockMvc mockMvc;

  @Autowired private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setupEach() throws Exception {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            // https://stackoverflow.com/questions/21495296/spring-mvc-controller-test-print-the-result-json-string
            // .alwaysDo(MockMvcResultHandlers.print())
            .build();
  }

  @Test
  public void whenCreateClipping_thenCheckNotificationsOk() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .put("important", true)
            .toString();

    this.mockMvc
        .perform(post("/clipping").contentType(MediaType.APPLICATION_JSON).content(clippingJson))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/notification"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].created_at", is(String.valueOf(LocalDate.now()))))
        .andExpect(jsonPath("$.content[0].viewed", is(false)))
        .andExpect(
            jsonPath(
                "$.content[0].description",
                is("Important publication '0=<br/>RECLAMANTE FULANO'")));
  }

  @Test
  public void whenCreateClipping_thenCheckNoNotificationsOk() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .put("important", false)
            .toString();

    this.mockMvc
        .perform(post("/clipping").contentType(MediaType.APPLICATION_JSON).content(clippingJson))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/notification"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content", hasSize(0)));
  }
}
