package com.appointment.publishing.controller;

import com.appointment.publishing.model.Clipping;
import com.appointment.publishing.repository.ClippingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClippingController {

  private ClippingRepository clippingRepository;

  public ClippingController(ClippingRepository clippingRepository) {
    this.clippingRepository = clippingRepository;
  }

  /**
   * {@code POST} end point to create a new clipping item on the database.
   *
   * <p>It receives a full {@link Clipping} object as a JSON representation, respecting the required
   * field from {@link Clipping}. Example of {@code POST} request body:
   *
   * <pre>{@code
   * {
   *     "clippingDate": "2020-06-24",
   *     "clippingMatter": "2020-06-24"
   * }
   * }</pre>
   */
  @PostMapping("/clipping")
  @ResponseStatus(HttpStatus.CREATED)
  public void addClipping(@RequestBody Clipping clipping) {
    clippingRepository.save(clipping);
  }

  /**
   * Given the query parameters as {@code /clipping?page=0&size=3} return the requested page items.
   *
   * @see org.springframework.data.domain.PageRequest for the available query parameters.
   * @see <a href="file:../resources/application.properties">application.properties</a> for page
   *     size limit configurations.
   * @see <a href="https://stackoverflow.com/questions/33018127/">Spring Data Rest - Sort by
   *     multiple properties</a> for sort paramters example on the input query.
   * @see <a href="https://www.baeldung.com/rest-api-pagination-in-spring">REST Pagination in
   *     Spring</a>.
   * @see <a href="https://stackoverflow.com/questions/47087415/">How can I use Pageable
   *     in @RestController?</a>.
   */
  @GetMapping("/clipping")
  public Page<Clipping> getClipping(Pageable pageable) {
    return clippingRepository.findAll(pageable);
  }
}
