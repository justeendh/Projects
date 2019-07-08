package com.common.irendercore.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyList<T> {

  private List<T> data = new ArrayList<>();

  private MyPage myPage = MyPage.of();
}
