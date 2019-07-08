package com.service.authservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

  private String userId;

  private String name;

  private String username;

  private String firstName;

  private String lastName;

  private String middleName;

  private Long birthday;

  private String email;

  private String phone;

  private String role;

  private String timeZone;

}
