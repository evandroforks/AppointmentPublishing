package com.appointment.publishing.service;

import com.appointment.publishing.model.Clipping;
import com.appointment.publishing.model.Notification;
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

  public ClippingService(
      ClippingRepository clippingRepository, NotificationRepository notificationRepository) {
    this.clippingRepository = clippingRepository;
    this.notificationRepository = notificationRepository;
  }

  public Clipping save(Clipping clipping) {
    if (clipping.isImportant()) {
      Notification notification = new Notification();
      notification.setCreated_at(LocalDate.now());
      notification.setDescription(
          String.format(
              "Important publication '%s=%s'", clipping.getId(), clipping.getClippingMatter()));
      notificationRepository.save(notification);
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
}
