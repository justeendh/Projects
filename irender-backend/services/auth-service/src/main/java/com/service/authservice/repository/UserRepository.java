package com.service.authservice.repository;

import com.common.irendersql.sql.repository.SqlBaseRepository;
import com.service.authservice.repository.entity.UserEntity;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends SqlBaseRepository<UserEntity> {

  UserEntity findTopByUserName(String userName);
}
