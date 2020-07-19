package com.appointment.publishing.repository;

import com.appointment.publishing.model.Clipping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClippingRepository extends JpaRepository<Clipping, Long> {}
