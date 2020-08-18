package com.upgrade.island.facade;

import com.upgrade.island.dto.AvailabilityDTO;
import com.upgrade.island.dto.BookingDTO;
import com.upgrade.island.dto.CreatedBookingDTO;
import com.upgrade.island.model.Booking;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IslandConverter {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;

  public static class AvailabilityDTOConverter implements Converter<List<LocalDate>, AvailabilityDTO>{

    @Override
    public AvailabilityDTO convert(final List<LocalDate> localDates) {
      List<String> availableDates = localDates.stream()
          .map(date -> dateTimeFormatter.format(date))
          .collect(Collectors.toList());
      return AvailabilityDTO.builder().dates(availableDates).build();
    }
  }

  public static class BookingDTOConverter implements Converter<Booking, BookingDTO> {

    @Override
    public BookingDTO convert(final Booking booking) {
      return BookingDTO.builder()
          .checkin(dateTimeFormatter.format(booking.getCheckin()))
          .checkout(dateTimeFormatter.format(booking.getCheckout()))
          .email(booking.getEmail())
          .username(booking.getUsername())
          .build();
    }
  }

  public static class CreatedBookingDTOConverter implements Converter<Integer, CreatedBookingDTO> {
    @Override
    public CreatedBookingDTO convert(final Integer bookingId) {
      return CreatedBookingDTO.builder()
          .bookingId(String.valueOf(bookingId))
          .build();
    }
  }

  public static Set<Converter> all() {
    return Set.of(
        new AvailabilityDTOConverter(),
        new BookingDTOConverter(),
        new CreatedBookingDTOConverter()
    );
  }
}
