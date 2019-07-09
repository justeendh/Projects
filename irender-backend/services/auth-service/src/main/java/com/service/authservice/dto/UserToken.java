package com.service.authservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserToken extends UserDto implements Serializable {

  public UserToken() {
  }

  public UserToken(UserDto userDto) {
    this.setUserId(userDto.getUserId());
    this.setUsername(userDto.getUsername());
    this.setFirstName(userDto.getFirstName());
    this.setLastName(userDto.getLastName());
    this.setMiddleName(userDto.getMiddleName());
    this.setBirthday(userDto.getBirthday());
    this.setEmail(userDto.getEmail());
    this.setPhone(userDto.getPhone());
    this.setTimeZone(userDto.getTimeZone());
  }

  private String accessToken = null;

  private String refreshToken = null;

  private Integer expiredTime = null;
}
