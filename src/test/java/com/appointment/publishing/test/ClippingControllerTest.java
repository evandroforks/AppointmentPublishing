package com.appointment.publishing.test;

import com.appointment.publishing.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
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
    String clippingJson = "{\"clippingDate\":\"2020-06-22\"}";

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/clipping"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].clippingDate", is("2020-06-22")));
  }
}
