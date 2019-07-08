package com.service.authservice.service;

import com.common.irendercore.service.BaseService;
import com.service.authservice.dto.LoginForm;
import com.service.authservice.dto.UserDto;

public interface UserService extends BaseService<UserDto> {

  UserDto findTopByUserName(String userName);

  UserDto login(LoginForm loginForm);
}
