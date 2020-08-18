package com.upgrade.island.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class Booking {

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private Integer id;
  private String username;
  private String email;
  private LocalDate checkin;
  private LocalDate checkout;
  @CreationTimestamp
  private LocalDateTime created;
  @UpdateTimestamp
  private LocalDateTime updated;
  private BookingStatus status;

}
