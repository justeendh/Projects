package com.service.authservice.repository;

import com.common.irendersql.sql.repository.SqlBaseRepository;
import com.service.authservice.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends SqlBaseRepository<UserEntity> {

  @Query(value = "SELECT u FROM UserEntity u WHERE u.userName = :userName")
  UserEntity findTopByUserName(@Param("userName") String userName);
}
