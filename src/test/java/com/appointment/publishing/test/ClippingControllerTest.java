package com.appointment.publishing.test;

import com.appointment.publishing.AppointmentPublishingApplication;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppointmentPublishingApplication.class)
@WebAppConfiguration
@Transactional // https://stackoverflow.com/questions/51036215/spring-h2-test-db-does-not-reset-before-each-test
public class ClippingControllerTest {

  private static final String CONTENT_TYPE = "application/json";

  private MockMvc mockMvc;

  @Autowired private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setup() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void whenCreateClipping_thenOk() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .toString();

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/clipping"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].clippingDate", is("2020-06-22")))
        .andExpect(jsonPath("$[0].clippingMatter", is("<br/>RECLAMANTE FULANO")))
        .andExpect(jsonPath("$[0].classificationType").doesNotExist());
  }

  @Test
  public void whenCreateClipping_thenFailure() throws Exception {
    String clippingJson =
        new JSONObject().put("clippingMatter", "<br/>RECLAMANTE FULANO").toString();

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$.message", is("Missing required property 'clippingDate'")));
  }

  @Test
  public void whenCreateClippingWithClassification_thenOk() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .put("classificationType", "HEARING")
            .toString();

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/clipping"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].clippingDate", is("2020-06-22")))
        .andExpect(jsonPath("$[0].clippingMatter", is("<br/>RECLAMANTE FULANO")))
        .andExpect(jsonPath("$[0].classificationType", is("HEARING")));
  }

  @Test
  public void whenCreateClippingWithClassifiedDate_thenOk() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .put("classifiedDate", "2020-06-25")
            .toString();

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/clipping"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].clippingDate", is("2020-06-22")))
        .andExpect(jsonPath("$[0].clippingMatter", is("<br/>RECLAMANTE FULANO")))
        .andExpect(jsonPath("$[0].classifiedDate", is("2020-06-25")));
  }
}
