package com.upgrade.island.service;

import com.upgrade.island.exception.BookingNotFoundException;
import com.upgrade.island.exception.CancelledBookingException;
import com.upgrade.island.exception.NotAvailableDatesException;
import com.upgrade.island.model.Booking;
import com.upgrade.island.model.BookingStatus;
import com.upgrade.island.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

public class IslandBookingServiceTest {

  private static final String USERNAME = "johndoe";
  private static final String EMAIL = "johndoe@upgrade.com";
  private static final String NOT_AVAILABLE_DATES_ERROR_MSG = "Requested dates checkin: %s checkout: %s is not available";
  private static final String CANCELLED_BOOKING_ERROR_MSG = "The booking with Id %s is canceled";
  private static final String BOOKING_NOT_FOUND_ERROR_MSG = "The booking with Id %s was not found";

  private BookingRepository repository;
  private IslandBookingService service;

  @BeforeEach
  public void init() {
    repository = Mockito.mock(BookingRepository.class);
    service = new IslandBookingService(repository);
  }

  @Test
  public void bookOneDay() {
    LocalDate checkin = LocalDate.now().plusDays(1);
    LocalDate checkout = checkin.plusDays(1);
    Mockito.when(repository.findActiveInDates(checkin, checkout, Mockito.any(Integer.class))).thenReturn(Optional.empty());
    Booking booking = service.createBooking(USERNAME, EMAIL, checkin, checkout);
    Assertions.assertNotNull(booking);
    Assertions.assertEquals(USERNAME, booking.getUsername());
    Assertions.assertEquals(EMAIL, booking.getEmail());
    Assertions.assertEquals(checkin, booking.getCheckin());
    Assertions.assertEquals(checkout, booking.getCheckout());
  }

  @Test
  public void bookThreeDays() {
    LocalDate checkin = LocalDate.now().plusDays(1);
    LocalDate checkout = checkin.plusDays(3);
    Booking booking = service.createBooking(USERNAME, EMAIL, checkin, checkout);
    Assertions.assertNotNull(booking);
    Assertions.assertEquals(USERNAME, booking.getUsername());
    Assertions.assertEquals(EMAIL, booking.getEmail());
    Assertions.assertEquals(checkin, booking.getCheckin());
    Assertions.assertEquals(checkout, booking.getCheckout());
  }

  @Test
  public void failBookFourDays() {
    LocalDate checkin = LocalDate.now().plusDays(1);
    LocalDate checkout = checkin.plusDays(4);
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        service.createBooking(USERNAME, EMAIL, checkin, checkout));
  }

  @Test
  public void failBookUnavailableDate() {
    LocalDate checkin = LocalDate.now().plusDays(1);
    LocalDate checkout = checkin.plusDays(1);
    Mockito.when(repository.findActiveInDates(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(Integer.class)))
        .thenReturn(Optional.of(new Booking()));
    NotAvailableDatesException ex = Assertions.assertThrows(NotAvailableDatesException.class, () ->
        service.createBooking(USERNAME, EMAIL, checkin, checkout));
    Assertions.assertEquals(String.format(NOT_AVAILABLE_DATES_ERROR_MSG, checkin, checkout), ex.getMessage());
  }

  @Test
  public void updateBooking() {
    Booking booking = new Booking();
    LocalDate checkin = LocalDate.now().plusDays(1);
    LocalDate checkout = checkin.plusDays(1);
    Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
    Mockito.when(repository.findActiveInDates(checkin, checkout, booking.getId())).thenReturn(Optional.empty());
    service.updateBooking(1, checkin, checkout);
  }

  @Test
  public void failUpdateBooking() {
    Booking booking = new Booking();
    LocalDate checkin = LocalDate.now().plusDays(1);
    LocalDate checkout = checkin.plusDays(1);
    Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
    Mockito.when(repository.findActiveInDates(checkin, checkout, booking.getId())).thenReturn(Optional.of(booking));
    NotAvailableDatesException ex = Assertions.assertThrows(NotAvailableDatesException.class, () ->
        service.updateBooking(1, checkin, checkout));
    Assertions.assertEquals(String.format(NOT_AVAILABLE_DATES_ERROR_MSG, checkin, checkout), ex.getMessage());
  }

  @Test
  public void getCanceledBooking() {
    Booking booking = new Booking();
    booking.setStatus(BookingStatus.CANCELED);
    Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
    CancelledBookingException ex = Assertions.assertThrows(CancelledBookingException.class, () ->
        service.getBooking(1));
    Assertions.assertEquals(String.format(CANCELLED_BOOKING_ERROR_MSG, 1), ex.getMessage());
  }

  @Test
  public void notFoundBooking() {
    Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
    BookingNotFoundException ex = Assertions.assertThrows(BookingNotFoundException.class, () ->
        service.getBooking(1));
    Assertions.assertEquals(String.format(BOOKING_NOT_FOUND_ERROR_MSG, 1), ex.getMessage());
  }
}
