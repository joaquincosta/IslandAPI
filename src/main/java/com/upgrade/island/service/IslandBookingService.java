package com.upgrade.island.service;

import com.upgrade.island.exception.BookingNotFoundException;
import com.upgrade.island.exception.CancelledBookingException;
import com.upgrade.island.exception.NotAvailableDatesException;
import com.upgrade.island.model.Booking;
import com.upgrade.island.model.BookingStatus;
import com.upgrade.island.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IslandBookingService {

  private final BookingRepository repository;

  public Booking createBooking(final String username, final String email,
                               final LocalDate checkin, final LocalDate checkout) {
    checkStay(checkin, checkout);
    Booking booking = new Booking();
    booking.setUsername(username);
    booking.setEmail(email);
    booking.setStatus(BookingStatus.BOOKED);
    checkAndSave(booking, checkin, checkout);
    return booking;
  }

  public Booking getBooking(final Integer bookingId) {
    Optional<Booking> optionalBooking = repository.findById(bookingId);
    return getOptionalBooking(optionalBooking, bookingId);
  }

  public void deleteBooking(final Integer bookingId) {
      Booking booking = getOptionalBooking(repository.findById(bookingId), bookingId);
      booking.setStatus(BookingStatus.CANCELED);
      repository.save(booking);
  }

  public void updateBooking(final Integer bookingId, final LocalDate newCheckin, final LocalDate newCheckout) {
    checkStay(newCheckin, newCheckout);
    Booking booking = getOptionalBooking(repository.findById(bookingId), bookingId);
    checkAndSave(booking, newCheckin, newCheckout);
  }

  @Transactional
  protected void checkAndSave(final Booking booking, final LocalDate checkin, final LocalDate checkout) {
    Optional<Booking> booked = repository.findActiveInDates(checkin, checkout);
    if (booked.isPresent()) {
      throw new NotAvailableDatesException(checkin, checkout);
    }
    booking.setCheckin(checkin);
    booking.setCheckout(checkout);
    repository.save(booking);
  }

  private Booking getOptionalBooking(final Optional<Booking> optionalBooking, final Integer bookingId) {
    if (optionalBooking.isEmpty()) {
      throw new BookingNotFoundException(bookingId);
    }
    Booking booking = optionalBooking.get();
    if (BookingStatus.CANCELED.equals(booking.getStatus())) {
      throw new CancelledBookingException(bookingId);
    }
    return booking;
  }

  private void checkStay(LocalDate checkin, LocalDate checkout) {
    Assert.isTrue(checkin.datesUntil(checkout).count() <= 3, "Stay cannot be more than 3 days");
  }



}
