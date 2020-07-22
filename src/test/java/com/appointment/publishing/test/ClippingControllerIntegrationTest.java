package com.appointment.publishing.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Runs Integration tests which could not be run due to bugs on MockMvc class.
 *
 * @see ClippingControllerTest for the MockMvc tests.
 * @see <a href="https://spring.io/guides/gs/testing-web/">Testing the Web Layer</a>.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext // https://stackoverflow.com/questions/14343893/how-do-i-reset-my-database-state-after-each-unit-test-without-making-the-whole-t
public class ClippingControllerIntegrationTest {

  /** The Server Address + Port to run the integration tests requests. */
  String url;

  /** Convert String objects to JSON. */
  ObjectMapper mapper;

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @BeforeAll
  public void setup() {
    url = "http://127.0.0.1:" + port;
    mapper = new ObjectMapper();

    // https://stackoverflow.com/questions/29447382/resttemplate-patch-request
    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }

  private ResponseEntity<String> doPost(String path, JSONObject body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(body.toString(), headers);
    return restTemplate.postForEntity(url + path, entity, String.class);
  }

  private ResponseEntity<String> doGet(String path) {
    return restTemplate.getForEntity(url + path, String.class);
  }

  private ResponseEntity<String> doPatch(String path, JSONObject body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(body.toString(), headers);

    // https://stackoverflow.com/questions/55323510/why-there-is-no-patchforentity-method-just-like-postforentity-in-resttemplate
    return restTemplate.exchange(url + path, HttpMethod.PATCH, entity, String.class);
  }

  /** @see ClippingControllerTest#whenCreateClippingByItsId_thenOk for the original test. */
  @Test
  public void whenCreateClippingByItsId_thenOk() throws Exception {
    ResponseEntity<String> query =
        doPost(
            "/clipping",
            new JSONObject()
                .put("clippingDate", "2020-06-12")
                .put("clippingMatter", "<br/>RECLAMANTE FULANO"));
    assertThat(query.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    ResponseEntity<String> response = doGet("/clipping/1");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    JsonNode responseJson = mapper.readTree(response.getBody());
    assertThat(responseJson.get("clippingDate").textValue()).isEqualTo("2020-06-12");
  }

  @Test
  public void whenUpdatingClipping_thenNotSupported() throws Exception {
    ResponseEntity<String> query =
        doPost(
            "/clipping",
            new JSONObject()
                .put("clippingDate", "2020-07-12")
                .put("clippingMatter", "<br/>RECLAMANTE FULANO")
                .put("viewed", false));
    assertThat(query.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    ResponseEntity<String> response =
        doPatch("/clipping/1", new JSONObject().put("important", "true"));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);

    JsonNode responseJson = mapper.readTree(response.getBody());
    assertThat(responseJson.get("message").asText())
        .isEqualTo("It is not yet supported to update the item 'id=1' with {important=true}!");
  }

  @Test
  public void whenUpdatingClipping_thenBadRequest() throws Exception {
    ResponseEntity<String> query =
        doPost(
            "/clipping",
            new JSONObject()
                .put("clippingDate", "2020-07-12")
                .put("clippingMatter", "<br/>RECLAMANTE FULANO")
                .put("viewed", false));
    assertThat(query.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    ResponseEntity<String> response =
        doPatch("/clipping/1", new JSONObject().put("viewed", "true"));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    JsonNode responseJson = mapper.readTree(response.getBody());
    assertThat(responseJson.get("message").asText())
        .isEqualTo(
            "Expecting a boolean value for the PATCH request for the item 'id=1' {viewed=true}!");
  }
}
