package com.appointment.publishing.service;

import com.appointment.publishing.model.Appointment;
import com.appointment.publishing.model.ClassificationType;
import com.appointment.publishing.model.Clipping;
import com.appointment.publishing.model.Notification;
import com.appointment.publishing.repository.AppointmentRepository;
import com.appointment.publishing.repository.ClippingRepository;
import com.appointment.publishing.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controls the clipping, notification and appointment business logic.
 *
 * @see <a
 *     href="https://www.tutorialspoint.com/spring_boot/spring_boot_service_components.htm">Spring
 *     Boot - Service Components</a>.
 */
@Service
public class ClippingService {

  private ClippingRepository clippingRepository;

  private NotificationRepository notificationRepository;

  private AppointmentRepository appointmentRepository;

  public ClippingService(
      ClippingRepository clippingRepository,
      NotificationRepository notificationRepository,
      AppointmentRepository appointmentRepository) {

    this.clippingRepository = clippingRepository;
    this.notificationRepository = notificationRepository;
    this.appointmentRepository = appointmentRepository;
  }

  public Clipping save(Clipping clipping) {
    if (clipping.isImportant()) {
      Notification notification = new Notification();
      notification.setCreated_at(LocalDate.now());
      notification.setDescription(
          String.format("Important publication '%s'", clipping.getClippingMatter()));
      notificationRepository.save(notification);
    }
    if (clipping.getClassificationType() == ClassificationType.HEARING) {
      Appointment appointment = new Appointment();
      LocalDate classifiedDate = clipping.getClassifiedDate();

      if (classifiedDate == null) {
        LocalDate clippingDate = clipping.getClippingDate();
        LocalDate localDate = clippingDate.plusDays(3);

        appointment.setDueDate(localDate);
        appointment.setDescription(
            String.format(
                "Appointment created from the clipping date + 3 days: '%s'",
                clipping.getClippingMatter()));
      } else {
        appointment.setDueDate(classifiedDate);
        appointment.setDescription(
            String.format(
                "Appointment created directly from the clipping classified date: '%s'",
                clipping.getClippingMatter()));
      }
      appointment.setCreated_at(LocalDate.now());
      appointmentRepository.save(appointment);
    }
    return clippingRepository.save(clipping);
  }

  public Optional<Clipping> findById(Long id) {
    return clippingRepository.findById(id);
  }

  public Page<Clipping> findAllClippings(Pageable pageable) {
    return clippingRepository.findAll(pageable);
  }

  public void deleteAll(Page<Clipping> all) {
    clippingRepository.deleteAll(all);
  }

  public void deleteAll() {
    clippingRepository.deleteAll();
  }

  public void delete(Clipping clipping) {
    clippingRepository.delete(clipping);
  }

  public Page<Notification> findAllNotifications(Pageable pageable) {
    return notificationRepository.findAll(pageable);
  }

  public Page<Appointment> findAllAppointments(Pageable pageable) {
    return appointmentRepository.findAll(pageable);
  }
}
