package com.upgrade.island.service;

import com.upgrade.island.model.Booking;
import com.upgrade.island.model.BookingStatus;
import com.upgrade.island.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.upgrade.island.utils.BookingUtils.dateOverlapped;

@RequiredArgsConstructor
@Service
public class IslandAvailabilityService {

  private final BookingRepository repository;

  public List<LocalDate> getAvailability(final String from, final String to) {
    LocalDate fromDate = Objects.isNull(from) ? LocalDate.now().plusDays(1) : LocalDate.parse(from);
    LocalDate toDate = Objects.isNull(from) ? LocalDate.now().plusDays(30) : LocalDate.parse(to);
    Assert.isTrue(fromDate.compareTo(toDate) < 0, "The  must be before to");
    Assert.isTrue(toDate.compareTo(LocalDate.now().plusDays(31)) < 0, "Cannot book more than 30 days before");
    List<Booking> bookings = repository.findInRange(fromDate, toDate, BookingStatus.BOOKED);
    return fromDate.datesUntil(toDate.plusDays(1))
        .filter(date -> bookings.stream().noneMatch(booking -> dateOverlapped(date,booking)))
        .collect(Collectors.toList());
  }
}
