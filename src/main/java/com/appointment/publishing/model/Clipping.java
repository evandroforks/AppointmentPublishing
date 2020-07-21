package com.appointment.publishing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDate;

/** Values used for the Clipping Rest API requests. */
enum ClassificationType {
  HEARING,
  DEADLINE
}

@Entity
public class Clipping {

  /** The clipping object primary key automatically generated. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  /** The clipping matter, as a SQL largest string type (required). */
  @Lob
  @Column(nullable = false)
  private String clippingMatter;

  /**
   * The clipping date as a SQL Date object (required).
   *
   * @see <a href="https://stackoverflow.com/questions/2305973">java.util.Date vs java.sql.Date</a>
   * @see <a href="https://stackoverflow.com/questions/28276126/">java.util.Date is generating a
   *     wrong date?</a>
   */
  @Column(nullable = false)
  private LocalDate clippingDate;

  /**
   * The clipping classification type, as a SQL enum type (optional).
   *
   * @see ClassificationType for possible values
   */
  private ClassificationType classificationType;

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

  public ClassificationType getClassificationType() {
    return classificationType;
  }

  public void setClassificationType(ClassificationType classificationType) {
    this.classificationType = classificationType;
  }
}
