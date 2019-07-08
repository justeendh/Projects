package com.common.irendercore.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {
  private String message;

  public ErrorResponse(String message) {
    this.message = message;
  }
}
