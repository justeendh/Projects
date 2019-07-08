package com.common.irendercore.model;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
  private String userId;
  private String username;
  private List<String> roles;
}
