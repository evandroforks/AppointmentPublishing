package com.appointment.publishing.controller;

import com.appointment.publishing.model.Clipping;
import com.appointment.publishing.repository.ClippingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

  @GetMapping("/clipping")
  public List<Clipping> getClipping() {
    return clippingRepository.findAll();
  }
}
