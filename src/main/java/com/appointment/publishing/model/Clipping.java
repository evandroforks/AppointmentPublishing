package com.appointment.publishing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDate;

@Entity
public class Clipping {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Lob
  @Column(nullable = false)
  private String clippingMatter;

  // Reason for usage of LocalDate:
  // https://stackoverflow.com/questions/2305973/java-util-date-vs-java-sql-date
  // https://stackoverflow.com/questions/28276126/java-util-date-is-generating-a-wrong-date
  @Column(nullable = false)
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

  public String getClippingMatter() {
    return clippingMatter;
  }

  public void setClippingMatter(String clippingMatter) {
    this.clippingMatter = clippingMatter;
  }
}
