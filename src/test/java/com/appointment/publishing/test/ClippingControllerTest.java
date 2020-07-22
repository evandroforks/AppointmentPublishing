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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            // https://stackoverflow.com/questions/21495296/spring-mvc-controller-test-print-the-result-json-string
            // .alwaysDo(MockMvcResultHandlers.print())
            .build();
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
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].clippingDate", is("2020-06-22")))
        .andExpect(jsonPath("$.content[0].clippingMatter", is("<br/>RECLAMANTE FULANO")))
        .andExpect(jsonPath("$.content[0].classificationType").doesNotExist());
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
  public void whenCreateClippingWithUnknownParameters() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .put("classificationUnknown", "NonExistent")
            .toString();

    ResultActions result =
        this.mockMvc
            .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
            .andExpect(status().isBadRequest());

    /**
     * `jsonPath()` should be used instead of `status()` but mockMvc is bugged because it eats the
     * response headers and the response body by putting the main message on the `errorMessage`
     * variable inside `mvcResult` from the MockMvc.class and ignoring/removing the Content Type =
     * application/json.
     *
     * @see <a href="https://stackoverflow.com/questions/25288930">mockMvc - Test Error Message</a>
     */
    // result.andExpect(
    //     jsonPath("$.message", is("Unknown request property 'classificationUnknown'")));
    result.andExpect(
        status().reason(containsString("Unknown request property 'classificationUnknown'")));
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
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].clippingDate", is("2020-06-22")))
        .andExpect(jsonPath("$.content[0].clippingMatter", is("<br/>RECLAMANTE FULANO")))
        .andExpect(jsonPath("$.content[0].classificationType", is("HEARING")));
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
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].clippingDate", is("2020-06-22")))
        .andExpect(jsonPath("$.content[0].clippingMatter", is("<br/>RECLAMANTE FULANO")))
        .andExpect(jsonPath("$.content[0].classifiedDate", is("2020-06-25")));
  }

  @Test
  public void whenCreateClippingWithClassifiedTime_thenOk() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .put("classifiedTime", "10:00")
            .toString();

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/clipping"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].clippingDate", is("2020-06-22")))
        .andExpect(jsonPath("$.content[0].clippingMatter", is("<br/>RECLAMANTE FULANO")))
        .andExpect(jsonPath("$.content[0].classifiedTime", is("10:00:00")));
  }

  @Test
  public void whenCreateClippingWithImportantFlag_thenOk() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .put("important", true)
            .toString();

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/clipping"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].clippingDate", is("2020-06-22")))
        .andExpect(jsonPath("$.content[0].clippingMatter", is("<br/>RECLAMANTE FULANO")))
        .andExpect(jsonPath("$.content[0].important", is(true)));
  }

  @Test
  public void whenCreateClippingWithViewedFlag_thenOk() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .put("viewed", true)
            .toString();

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/clipping"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].clippingDate", is("2020-06-22")))
        .andExpect(jsonPath("$.content[0].clippingMatter", is("<br/>RECLAMANTE FULANO")))
        .andExpect(jsonPath("$.content[0].viewed", is(true)));
  }

  public void createClippingSamplePages() throws Exception {
    String clippingJson13 =
        new JSONObject()
            .put("clippingDate", "2020-06-13")
            .put("clippingMatter", "<br/>RECLAMANTE Dia 13")
            .toString();

    String clippingJson14 =
        new JSONObject()
            .put("clippingDate", "2020-06-14")
            .put("clippingMatter", "<br/>RECLAMANTE Dia 14")
            .toString();

    String clippingJson15 =
        new JSONObject()
            .put("clippingDate", "2020-06-15")
            .put("clippingMatter", "<br/>RECLAMANTE Dia 15")
            .toString();

    String clippingJson16 =
        new JSONObject()
            .put("clippingDate", "2020-06-16")
            .put("clippingMatter", "<br/>RECLAMANTE Dia 16")
            .toString();

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson13))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson14))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson15))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson16))
        .andExpect(status().isCreated());
  }

  @Test
  public void whenCreateClippingWithPagination_thenOk() throws Exception {
    createClippingSamplePages();

    this.mockMvc
        .perform(get("/clipping?page=2&size=1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].clippingDate", is("2020-06-15")))
        .andExpect(jsonPath("$.content[0].clippingMatter", is("<br/>RECLAMANTE Dia 15")))
        .andExpect(jsonPath("$.number", is(2)))
        .andExpect(jsonPath("$.totalPages", is(4)))
        .andExpect(jsonPath("$.totalElements", is(4)));
  }

  @Test
  public void whenCreateClippingByItsId_thenOk() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .toString();

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
        .andExpect(status().isCreated());

    /**
     * Test disabled because mockMvc is bugged and it does not find the object on the database when
     * all tests are run together. This test only works when running it alone, i.e., when running
     * only one test.
     *
     * <p>To overcome this situation, a Integration test was created for this on {@link
     * ClippingControllerIntegrationTest#whenCreateClippingByItsId_thenOk}.
     */
    // this.mockMvc
    //     .perform(get("/clipping/1"))
    //     .andExpect(status().isOk())
    //     .andExpect(content().contentType(CONTENT_TYPE))
    //     .andExpect(jsonPath("$.clippingDate", is("2020-06-22")))
    //     .andExpect(jsonPath("$.clippingMatter", is("<br/>RECLAMANTE FULANO")));
  }

  @Test
  public void whenUpdatingClipping_thenOK() throws Exception {
    String clippingJson =
        new JSONObject()
            .put("clippingDate", "2020-06-22")
            .put("clippingMatter", "<br/>RECLAMANTE FULANO")
            .put("viewed", false)
            .toString();

    String updateJson = new JSONObject().put("viewed", true).toString();

    this.mockMvc
        .perform(post("/clipping").contentType(CONTENT_TYPE).content(clippingJson))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(patch("/clipping/1").contentType(CONTENT_TYPE).content(updateJson))
        .andExpect(status().isOk());

    this.mockMvc
        .perform(get("/clipping"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].clippingDate", is("2020-06-22")))
        .andExpect(jsonPath("$.content[0].viewed", is(true)));
  }
}
