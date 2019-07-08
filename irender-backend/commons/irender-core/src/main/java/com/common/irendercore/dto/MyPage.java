package com.common.irendercore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "of")
@Data
public class MyPage {

  private Integer size = null;

  private Integer number = null;

  private Long totalElements = null;

  private Integer totalPages = null;

  public static MyPage of(){
    return of(0, 0, 0L, 0);
  }

  public static MyPage of(int size, int number, long totalElements){
    if(size != 0){
      return of(size, number, totalElements, (int)totalElements/size);
    }else {
      return of(size, number, totalElements, 0);
    }
  }
}

