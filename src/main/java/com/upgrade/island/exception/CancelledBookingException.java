package com.upgrade.island.exception;

public class CancelledBookingException extends RuntimeException {

  private static final String ERROR_MSG = "The booking with Id %s is canceled";

  public CancelledBookingException(final Integer bookingId) {
    super(String.format(ERROR_MSG, bookingId));
  }
}
