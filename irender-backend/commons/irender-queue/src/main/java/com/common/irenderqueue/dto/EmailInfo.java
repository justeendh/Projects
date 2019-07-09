package com.common.irenderqueue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailInfo implements Serializable {

  private List<String> to;

  private List<String> cc;

  private List<String> bcc;

  private String subject;

  private String content;
}
