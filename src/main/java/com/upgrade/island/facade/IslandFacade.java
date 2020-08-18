package com.upgrade.island.facade;

import com.upgrade.island.dto.AvailabilityDTO;
import com.upgrade.island.dto.BookingDTO;
import com.upgrade.island.dto.CreatedBookingDTO;
import com.upgrade.island.dto.UpdateBody;
import com.upgrade.island.model.Booking;
import com.upgrade.island.service.IslandAvailabilityService;
import com.upgrade.island.service.IslandBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Component
public class IslandFacade {

  private final IslandBookingService islandBookingService;
  private final IslandAvailabilityService islandAvailabilityService;
  private final ConversionService conversionService;


  public AvailabilityDTO getAvailability(final String from, final String to) {
    List<LocalDate> availability = islandAvailabilityService.getAvailability(from, to);
    return conversionService.convert(availability, AvailabilityDTO.class);
  }


  public CreatedBookingDTO createBooking(final BookingDTO body) {
    Assert.notNull(body, "The create booking body cannot be null");
    Assert.hasLength(body.getCheckin(), "the checkin cannot be empty");
    Assert.hasLength(body.getCheckout(), "The checkout cannot be empty");
    Assert.hasLength(body.getUsername(), "the username cannot be empty");
    Assert.hasLength(body.getEmail(), "the email cannot be empty");
    Booking booking = islandBookingService.createBooking(body.getUsername(), body.getEmail(),
        LocalDate.parse(body.getCheckin(), DateTimeFormatter.ISO_DATE),
        LocalDate.parse(body.getCheckout(), DateTimeFormatter.ISO_DATE));
    return conversionService.convert(booking.getId(), CreatedBookingDTO.class);
  }

  public BookingDTO getBooking(final String bookingId) {
    Assert.notNull(bookingId, "the bookingId cannot be null");
    Booking booking = islandBookingService.getBooking(Integer.valueOf(bookingId));
    return conversionService.convert(booking, BookingDTO.class);
  }


  public void modifyBooking(final String bookingId, final UpdateBody body) {
    Assert.notNull(bookingId, "the bookingId cannot be null");
    Assert.notNull(body, "the UpdateBody cannot be null");
    Assert.hasLength(body.getCheckin(), "the update checkin cannot be empty");
    Assert.hasLength(body.getCheckout(), "the update checkout cannot be empty");
    islandBookingService.updateBooking(Integer.valueOf(bookingId),
        LocalDate.parse(body.getCheckin(), DateTimeFormatter.ISO_DATE),
        LocalDate.parse(body.getCheckout(), DateTimeFormatter.ISO_DATE));
  }

  public void deleteBooking(final String bookingId) {
    Assert.notNull(bookingId, "the bookingId cannot be null");
    islandBookingService.deleteBooking(Integer.valueOf(bookingId));
  }

}
