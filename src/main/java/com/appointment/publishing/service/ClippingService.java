package com.appointment.publishing.service;

import com.appointment.publishing.model.Clipping;
import com.appointment.publishing.repository.ClippingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

  public ClippingService(ClippingRepository clippingRepository) {
    this.clippingRepository = clippingRepository;
  }

  public Clipping save(Clipping clipping) {
    return clippingRepository.save(clipping);
  }

  public Optional<Clipping> findById(Long id) {
    return clippingRepository.findById(id);
  }

  public Page<Clipping> findAll(Pageable pageable) {
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
}
