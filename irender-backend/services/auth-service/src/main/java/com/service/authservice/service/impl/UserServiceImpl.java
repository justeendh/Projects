package com.service.authservice.service.impl;

import com.common.irendersql.irender.sql.mapper.BaseMapper;
import com.common.irendersql.sql.repository.SqlBaseRepository;
import com.service.authservice.dto.LoginForm;
import com.service.authservice.dto.UserDto;
import com.service.authservice.mapper.UserMapper;
import com.service.authservice.repository.UserRepository;
import com.service.authservice.repository.entity.UserEntity;
import com.service.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.common.irendercore.service.impl.SqlBaseServiceImpl;

@Service
@Slf4j
public class UserServiceImpl extends SqlBaseServiceImpl<UserDto, UserEntity> implements UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  private final BCryptPasswordEncoder passwordEncoder;

  private static final String ERROR_MESSAGE = "Username or password invalid!";

  @Autowired
  public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected SqlBaseRepository<UserEntity> getBaseRepository() {
    return userRepository;
  }

  @Override
  protected BaseMapper<UserDto, UserEntity> getBaseMapper() {
    return userMapper;
  }

  @Override
  public UserDto findTopByUserName(String userName) {
    UserEntity entity = userRepository.findTopByUserName(userName);
    return entity == null ? null : userMapper.entityToDto(entity);
  }

  @Override
  public UserDto login(LoginForm loginForm) {
    UserEntity userEntity = userRepository.findTopByUserName(loginForm.getUsername());
    if (userEntity == null || StringUtils.isEmpty(loginForm.getPassword()) || !passwordEncoder.matches(loginForm.getPassword(), userEntity.getPassword())) {
      throw new IllegalArgumentException(ERROR_MESSAGE);
    } else {
      return userMapper.entityToDto(userEntity);
    }
  }

  //  @PostConstruct
  public void init() {
    UserEntity admin = UserEntity.builder().userName("admin").password(passwordEncoder.encode("admin")).build();
    UserEntity member = UserEntity.builder().userName("member").password(passwordEncoder.encode("member")).build();
    userRepository.save(member);
    userRepository.save(admin);

  }
}
