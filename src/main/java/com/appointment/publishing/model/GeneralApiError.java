package com.appointment.publishing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/** Simple JavaBean for standard error messages responses. */
public class GeneralApiError {

  /** The HTTP code of this object. */
  private HttpStatus status;

  /** The date-time instance of when the error happened. */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;

  /** A user-friendly message about the error. */
  private String message;

  /** Describes the error in more detail. */
  private String debugMessage;

  /** A constructor to initialize the timestamp. */
  private GeneralApiError() {
    timestamp = LocalDateTime.now();
  }

  /**
   * Constructor with a response status and a reason to add to the exception message as explanation,
   * as well as a nested exception.
   *
   * @param status the HTTP status (required)
   * @param reason the associated reason (required)
   * @param cause a nested exception (required)
   */
  public GeneralApiError(HttpStatus status, String reason, Throwable cause) {
    this();
    this.status = status;
    this.message = reason;
    this.debugMessage = cause.getLocalizedMessage();
  }

  public HttpStatus getStatus() {
    return status;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }

  public String getDebugMessage() {
    return debugMessage;
  }
}
