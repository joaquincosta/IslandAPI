package com.upgrade.island.exception;

public class BookingNotFoundException extends RuntimeException {
  private static final String ERROR_MSG = "The booking with Id %s was not found";

  public BookingNotFoundException(final Integer bookingId) {
    super(String.format(ERROR_MSG, bookingId));
  }
}
