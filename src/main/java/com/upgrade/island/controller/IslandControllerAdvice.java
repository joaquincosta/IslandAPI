package com.upgrade.island.controller;

import com.upgrade.island.dto.ErrorDTO;
import com.upgrade.island.exception.BookingNotFoundException;
import com.upgrade.island.exception.CancelledBookingException;
import com.upgrade.island.exception.NotAvailableDatesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IslandControllerAdvice {

  @ExceptionHandler(value = {IllegalArgumentException.class,
      CancelledBookingException.class})
  protected ResponseEntity<ErrorDTO> handleBadRequest(final RuntimeException ex) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    return new ResponseEntity<>(createResponse(httpStatus, ex), httpStatus);
  }

  @ExceptionHandler(value = NotAvailableDatesException.class)
  protected ResponseEntity<ErrorDTO> handleGone(final RuntimeException ex) {
    HttpStatus httpStatus = HttpStatus.GONE;
    return new ResponseEntity<>(createResponse(httpStatus, ex), httpStatus);
  }

  @ExceptionHandler(value = {BookingNotFoundException.class})
  protected ResponseEntity<ErrorDTO> handleNotFound(final RuntimeException ex) {
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    return new ResponseEntity<>(createResponse(httpStatus, ex), httpStatus);
  }

  @ExceptionHandler(value = RuntimeException.class)
  protected ResponseEntity<ErrorDTO> genericHandle(final RuntimeException ex) {
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    return new ResponseEntity<>(createResponse(httpStatus, ex), httpStatus);
  }

  private ErrorDTO createResponse(final HttpStatus status, final Exception ex) {
    return ErrorDTO.builder().code(status.value()).message(ex.getMessage()).build();
  }

}
