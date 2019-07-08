package com.common.irendercore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyModel {

  String id;

  String name;

  String image;

  @JsonProperty("created_at")
  long createdAt;

  @JsonProperty("created_by")
  String createdBy;

  @JsonProperty("updated_at")
  long updatedAt;

  @JsonProperty("updated_by")
  String updatedBy;
}
