package com.appointment.publishing.controller;

import com.appointment.publishing.model.Clipping;
import com.appointment.publishing.repository.ClippingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

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
   * {@code PATCH} end point to update an existing clipping on the database.
   *
   * <p>It receives a JSON, respectively with the fields values to be updated.
   *
   * <p>As a first implementation, it only supports updating an existent clipping {@code viewed}.
   * Example of {@code PATCH} request body:
   *
   * <pre>{@code
   * {
   *     "viewed": true,
   * }
   * }</pre>
   *
   * @throws ResponseStatusException with HttpStatus.NOT_IMPLEMENTED if not called with the {@code
   *     viewed} field.
   * @throws ResponseStatusException with HttpStatus.BAD_REQUEST if the {@code viewed} item is not a
   *     boolean value.
   */
  @PatchMapping("/clipping/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void updateClipping(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
    Optional<Clipping> item = clippingRepository.findById(id);

    if (!item.isPresent()) {
      final String missing = String.format("The item with 'id=%s' is not existent!", id);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missing);
    }
    Clipping clipping = item.get();

    // TODO: Use DomainObjectReader to dynamically update any object attribute
    // https://stackoverflow.com/questions/17860520/spring-mvc-patch-method-partial-updates
    // fields.forEach(
    //     (key, value) -> {
    //       Field field = ReflectionUtils.findField(Clipping.class, key);
    //       // ReflectionUtils.setField(field, clipping, v);
    //       PropertyAccessor myAccessor = PropertyAccessorFactory.forBeanPropertyAccess(clipping);
    //       myAccessor.setPropertyValue(field.getName(), value);
    //     });

    if (!fields.containsKey("viewed")) {
      final String missing =
          String.format(
              "It is not yet supported to update the item 'id=%s' with %s!", id, fields.toString());
      throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, missing);
    }
    Object viewed = fields.get("viewed");

    if (!(viewed instanceof Boolean)) {
      final String missing =
          String.format(
              "Expecting a boolean value for the PATCH request for the item 'id=%s' %s!",
              id, fields.toString());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missing);
    }
    clipping.setViewed((boolean) viewed);
    clippingRepository.save(clipping);
  }

  /**
   * In a {@code GET} request, given the query parameters as {@code /clipping?page=0&size=3} return
   * the requested page items.
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

  /**
   * In a {@code GET} request, return a clipping item from the database given its primary key.
   *
   * @throws ResponseStatusException if the item was not found.
   */
  @GetMapping("/clipping/{id}")
  public Clipping getClippingItem(@PathVariable("id") Long id) {
    Optional<Clipping> item = clippingRepository.findById(id);
    if (item.isPresent()) {
      return item.get();
    }
    final String missing = String.format("The item with 'id=%s' does not exit!", id);
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missing);
  }

  /**
   * Given query parameters in a {@code DELETE} request as {@code /clipping?page=0&size=3} delete
   * all requested page items from the database.
   *
   * @see org.springframework.data.domain.PageRequest for the available query parameters.
   * @see <a href="file:../resources/application.properties">application.properties</a> for page
   *     size limit configurations.
   * @see <a href="https://stackoverflow.com/questions/299628/">Is an entity body allowed for an
   *     HTTP DELETE request?</a>
   */
  @DeleteMapping("/clipping")
  public void deleteClipping(Pageable pageable) {
    Page<Clipping> all = clippingRepository.findAll(pageable);
    clippingRepository.deleteAll(all);
  }

  /**
   * In a {@code DELETE} request as {@code /clipping/all}, deletes all clippsing items from the
   * database.
   */
  @DeleteMapping("/clipping/all")
  public void deleteClipping() {
    clippingRepository.deleteAll();
  }

  /**
   * In a {@code DELETE} request as {@code /clipping/1}, delete the clipping item {@code 1} from the
   * database given {@code 1} its its primary key.
   *
   * @throws ResponseStatusException if the item was not found.
   */
  @DeleteMapping("/clipping/{id}")
  public void deleteClippingItem(@PathVariable("id") Long id) {
    Optional<Clipping> item = clippingRepository.findById(id);
    if (!item.isPresent()) {
      final String missing = String.format("The item with 'id=%s' does not exit!", id);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missing);
    }
    clippingRepository.delete(item.get());
  }
}
