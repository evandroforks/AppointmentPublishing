package com.appointment.publishing.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Clipping {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  // Reason for usage of LocalDate:
  // https://stackoverflow.com/questions/2305973/java-util-date-vs-java-sql-date
  // https://stackoverflow.com/questions/28276126/java-util-date-is-generating-a-wrong-date
  private LocalDate clippingDate;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public LocalDate getClippingDate() {
    return clippingDate;
  }

  public void setClippingDate(LocalDate clippingDate) {
    this.clippingDate = clippingDate;
  }
}
