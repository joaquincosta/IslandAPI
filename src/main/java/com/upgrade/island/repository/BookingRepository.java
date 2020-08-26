package com.upgrade.island.repository;

import com.upgrade.island.model.Booking;
import com.upgrade.island.model.BookingStatus;
import com.upgrade.island.utils.BookingUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer> {

  default Optional<Booking> findActiveInDates(final LocalDate checkin, final LocalDate checkout, Integer id) {
    Iterable<Booking> bookings = this.findAll();
    return StreamSupport.stream(bookings.spliterator(), false)
        .filter(booking -> BookingStatus.BOOKED.equals(booking.getStatus())
            && !booking.getId().equals(id)
            && BookingUtils.isOverlapped(checkin, checkout, booking))
        .findFirst();
  }

  @Query("select b from Booking b where b.status = :status and b.checkin between :from and :to or b.checkout between :from and :to order by b.checkin asc")
  List<Booking> findInRange(@Param("from") final LocalDate from, @Param("to") final LocalDate to
      , @Param("status") BookingStatus status);

}
