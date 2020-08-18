package com.upgrade.island.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AvailabilityDTO {
  private List<String> dates;
}
