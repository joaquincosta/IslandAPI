package com.upgrade.island.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingDTO {

  private String username;
  private String email;
  private String checkin;
  private String checkout;

}
