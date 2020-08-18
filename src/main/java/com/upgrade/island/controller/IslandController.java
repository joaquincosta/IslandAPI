package com.upgrade.island.controller;

import com.upgrade.island.dto.AvailabilityDTO;
import com.upgrade.island.dto.BookingDTO;
import com.upgrade.island.dto.CreatedBookingDTO;
import com.upgrade.island.dto.UpdateBody;
import com.upgrade.island.facade.IslandFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/island")
public class IslandController {

  private final IslandFacade islandFacade;

  @GetMapping("/availability")
  public ResponseEntity<AvailabilityDTO> getAvailability(@RequestParam(required = false) String from, @RequestParam(required = false) String to) {
    AvailabilityDTO dto = islandFacade.getAvailability(from, to);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PostMapping("/booking")
  public ResponseEntity<CreatedBookingDTO> createBooking(@RequestBody BookingDTO body) {
    CreatedBookingDTO dto = islandFacade.createBooking(body);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @GetMapping("/booking/{bookingId}")
  public ResponseEntity<BookingDTO> getBooking(@PathVariable String bookingId) {
    BookingDTO dto = islandFacade.getBooking(bookingId);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PutMapping("/booking/{bookingId}")
  public ResponseEntity modifyBooking(@PathVariable String bookingId, @RequestBody UpdateBody body) {
    islandFacade.modifyBooking(bookingId, body);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/booking/{bookingId}")
  public ResponseEntity deleteBooking(@PathVariable String bookingId) {
    islandFacade.deleteBooking(bookingId);
    return new ResponseEntity(HttpStatus.OK);
  }

}
