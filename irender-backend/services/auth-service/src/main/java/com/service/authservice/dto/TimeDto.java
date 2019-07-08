package com.service.authservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class TimeDto {
  private Long currentTime;

  @Builder
  public TimeDto(Long currentTime) {
    this.currentTime = currentTime;
  }
}
