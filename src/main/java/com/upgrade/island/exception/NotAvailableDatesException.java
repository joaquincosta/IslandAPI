package com.upgrade.island.exception;

import java.time.LocalDate;

public class NotAvailableDatesException extends RuntimeException {
  private static final String ERROR_MSG = "Requested dates checkin: %s checkout: %s is not available";

  public NotAvailableDatesException(final LocalDate checkin, final LocalDate checkout) {
    super(String.format(ERROR_MSG, checkin, checkout));
  }
}
