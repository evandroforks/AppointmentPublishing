package com.appointment.publishing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDate;

@Entity
public class Notification {

  /** The notification object primary key automatically generated. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** The notification description, as a SQL largest String type (required). */
  @Lob
  @Column(nullable = false)
  private String description;

  /** The notification creation date a SQL Date type (required). */
  @Column(nullable = false)
  private LocalDate created_at;

  /** The notification viewed flag as a SQL Boolean type (optional). */
  private boolean viewed;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDate created_at) {
    this.created_at = created_at;
  }

  public boolean isViewed() {
    return viewed;
  }

  public void setViewed(boolean viewed) {
    this.viewed = viewed;
  }
}
