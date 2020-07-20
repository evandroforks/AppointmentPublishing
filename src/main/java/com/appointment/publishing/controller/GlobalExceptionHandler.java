package com.appointment.publishing.controller;

import com.appointment.publishing.model.GeneralApiError;
import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Provides handling for exceptions throughout this service.
 *
 * <p>Automatically produces a user friendly response message as:
 *
 * <pre>{@code
 * {
 *     "status": "BAD_REQUEST",
 *     "timestamp": "20-07-2020 05:07:05",
 *     "message": "Missing required property 'clippingMatter'",
 *     "debugMessage": "not-null property ..."
 * }
 * }</pre>
 *
 * @see <a href="https://www.toptal.com/java/spring-boot-rest-api-error-handling">Guide to Spring
 *     Boot REST API Error Handling</a>
 * @see <a
 *     href="https://medium.com/@jovannypcg/understanding-springs-controlleradvice-cd96a364033f">Understanding
 *     Spring’s @ControllerAdvice</a>
 * @see <a href="https://www.baeldung.com/exception-handling-for-rest-with-spring">Error Handling
 *     for REST with Spring</a>
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(ClippingController.class);

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleDataIntegrityViolationException(
      Exception exception, WebRequest request) {

    PropertyValueException cause = (PropertyValueException) exception.getCause();
    final String property = cause.getPropertyName();
    final String missing = String.format("Missing required property '%s'", property);

    logger.error("{} due to {}", missing, cause.getLocalizedMessage());
    HttpHeaders headers = new HttpHeaders();
    return buildResponseEntity(
        new GeneralApiError(HttpStatus.BAD_REQUEST, missing, exception), headers);
  }

  protected ResponseEntity<Object> buildResponseEntity(GeneralApiError error, HttpHeaders headers) {
    return new ResponseEntity<>(error, headers, error.getStatus());
  }
}
