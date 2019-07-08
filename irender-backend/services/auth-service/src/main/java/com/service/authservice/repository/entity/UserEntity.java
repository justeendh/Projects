package com.service.authservice.repository.entity;

import com.common.irendersql.sql.repository.entity.MyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity extends MyEntity implements Serializable {
  @Id
  @Column(name = "id")
  private String id;

  @NotNull
  @Size(max = 50)
  @Column(name = "user_name")
  private String userName;

  @Size(max = 500)
  @Column(name = "password")
  private String password;

  @NotNull
  @Size(max = 20)
  @Column(name = "user_role")
  private String userRole;

  @Size(max = 50)
  @Column(name = "email")
  private String email;

  @Size(max = 10)
  @Column(name = "sex")
  private String sex;

  @Size(max = 20)
  @Column(name = "first_name")
  private String firstName;

  @Size(max = 20)
  @Column(name = "last_name")
  private String lastName;

  @Size(max = 50)
  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "birthday")
  private Long birthday;

  @Size(max = 20)
  @Column(name = "phone")
  private String phone;

  @Size(max = 2)
  @Column(name = "language")
  private String language;

  @Size(max = 10)
  @Column(name = "nationality")
  private String nationality;

  @Size(max = 10)
  @Column(name = "timezone")
  private String timezone;

  @Size(max = 100)
  @Column(name = "photo")
  private String photo;

  @Size(max = 100)
  @Column(name = "video")
  private String video;

  @Size(max = 200)
  @Column(name = "address")
  private String address;

  @Size(max = 2)
  @Column(name = "academic_degree")
  private String academicDegree;

  @Column(name = "level_id")
  private String levelId;

  @Column(name = "introduction", columnDefinition = "TEXT")
  private String introduction;

  @Column(name = "company_review", columnDefinition = "TEXT")
  private String companyReview;

  @NotNull
  @Column(name = "status")
  private Integer status;

  @Size(max = 100)
  @Column(name = "token")
  private String token;

  @Column(name = "token_expire")
  private Long tokenExpire;


}
