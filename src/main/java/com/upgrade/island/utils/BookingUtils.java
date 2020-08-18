package com.upgrade.island.utils;

import com.upgrade.island.model.Booking;

import java.time.LocalDate;

public class BookingUtils {

  public static Boolean isOverlapped(final LocalDate checkin, final LocalDate checkout, final Booking booking) {
    LocalDate bookingCheckin = booking.getCheckin();
    LocalDate bookingCheckout = booking.getCheckout();
    Boolean overlappedCheckin = checkin.compareTo(bookingCheckout) < 0 && checkin.compareTo(bookingCheckin) >= 0;
    Boolean overlappedCheckout = checkout.compareTo(bookingCheckin) > 0 && checkout.compareTo(bookingCheckout) <= 0;
    return overlappedCheckin || overlappedCheckout;
  }

  public static Boolean dateOverlapped(final LocalDate date, final Booking booking) {
    return date.compareTo(booking.getCheckin()) >= 0 && date.compareTo(booking.getCheckout()) < 0;
  }
}
