package com.service.authservice.mapper;

import com.common.irendersql.irender.sql.mapper.BaseMapper;
import com.service.authservice.dto.UserDto;
import com.service.authservice.repository.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;


@Component
public class UserMapper implements BaseMapper<UserDto, UserEntity> {


  ModelMapper mapper;

  PropertyMap<UserDto, UserEntity> mapDto2Entity = new PropertyMap<UserDto, UserEntity>() {
    protected void configure() {
      map().setId(source.getUserId());
      map().setTimezone(source.getTimeZone());
      map().setUserName(source.getUsername());
      map().setUserRole(source.getRole());
    }
  };
  PropertyMap<UserEntity, UserDto> mapEntity2Dto = new PropertyMap<UserEntity, UserDto>() {
    protected void configure() {
      map().setUserId(source.getId());
      map().setTimeZone(source.getTimezone());
      map().setUsername(source.getUserName());
      map().setRole(source.getUserRole());
    }
  };

  public UserMapper() {
    this.mapper = new ModelMapper();
  }

  @Override
  public UserDto entityToDto(UserEntity entity) {
    return mapper.map(entity, UserDto.class);
  }

  @Override
  public UserEntity dtoToEntity(UserDto dto) {
    return mapper.map(dto, UserEntity.class);
  }

  @Override
  public UserDto entityToDto(UserEntity entity, UserDto dto) {
    mapper.map(entity, dto);
    return dto;
  }

  @Override
  public UserEntity dtoToEntity(UserDto dto, UserEntity entity) {
    mapper.map(dto, entity);
    return entity;
  }
}
