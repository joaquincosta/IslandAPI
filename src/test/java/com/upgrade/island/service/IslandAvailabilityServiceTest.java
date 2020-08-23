package com.upgrade.island.service;

import com.upgrade.island.model.Booking;
import com.upgrade.island.model.BookingStatus;
import com.upgrade.island.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IslandAvailabilityServiceTest {

  private BookingRepository repository;
  private IslandAvailabilityService service;
  private DateTimeFormatter dtf;

  @BeforeEach
  public void init() {
    repository = Mockito.mock(BookingRepository.class);
    service = new IslandAvailabilityService(repository);
    dtf = DateTimeFormatter.ISO_DATE;
  }

  @Test
  public void testFullFreeDates() {
    Mockito.when(repository.findInRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(BookingStatus.class)))
        .thenReturn(List.of());
    List<LocalDate> dates = service.getAvailability(null, null);
    Assertions.assertEquals(30, dates.size());
  }

  @Test
  public void testTenFreeDates() {
    Mockito.when(repository.findInRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(BookingStatus.class)))
        .thenReturn(datesDays(20));
    List<LocalDate> dates = service.getAvailability(null, null);
    Assertions.assertEquals(10, dates.size());
  }

  @Test
  public void testNoAvailableDays() {
    Mockito.when(repository.findInRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(BookingStatus.class)))
        .thenReturn(datesDays(10));
    LocalDate today = LocalDate.now();
    List<LocalDate> dates = service.getAvailability(dtf.format(today.plusDays(1)), dtf.format(today.plusDays(10)));
    Assertions.assertEquals(0, dates.size());
  }

  @Test
  public void testFourAvailableDays() {
    Mockito.when(repository.findInRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(BookingStatus.class)))
        .thenReturn(datesDays(6));
    LocalDate today = LocalDate.now();
    List<LocalDate> dates = service.getAvailability(dtf.format(today.plusDays(1)), dtf.format(today.plusDays(10)));
    Assertions.assertEquals(4, dates.size());
  }


  private List<Booking> datesDays(Integer days) {
    LocalDate now = LocalDate.now();
    return IntStream.rangeClosed(1, days).mapToObj(i -> createDay(now.plusDays(i))).collect(Collectors.toList());
  }

  private Booking createDay(LocalDate date) {
    Booking booking = new Booking();
    booking.setCheckin(date);
    booking.setCheckout(date.plusDays(1));
    return booking;
  }

}
