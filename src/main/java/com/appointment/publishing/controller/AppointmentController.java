package com.appointment.publishing.controller;

import com.appointment.publishing.model.Appointment;
import com.appointment.publishing.service.ClippingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentController {

  private ClippingService clippingService;

  public AppointmentController(ClippingService clippingService) {
    this.clippingService = clippingService;
  }

  /**
   * In a {@code GET} request, given the query parameters as {@code /appointment?page=0&size=3}
   * return the requested page appointments.
   *
   * @see org.springframework.data.domain.PageRequest for the available query parameters.
   * @see <a href="file:../resources/application.properties">application.properties</a> for page
   *     size limit configurations.
   */
  @GetMapping("/appointment")
  public Page<Appointment> getAppointment(Pageable pageable) {
    return clippingService.findAllAppointments(pageable);
  }
}
