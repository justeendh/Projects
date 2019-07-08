package com.common.irendersql.sql.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@MappedSuperclass
public class MyEntity {

  @Id
  private String id;

  @Column(name = "created_at")
  private Long createdAt;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_at")
  private Long updatedAt;

  @Column(name = "updated_by")
  private String updatedBy;

  @PrePersist
  private void ensureId() {
    this.setId(UUID.randomUUID().toString());
    this.setCreatedAt(System.currentTimeMillis());
    this.setUpdatedAt(System.currentTimeMillis());
  }

  @PreUpdate
  private void setUpdatedAt() {
    this.setUpdatedAt(System.currentTimeMillis());
  }
}
