package com.common.irendercore.service;

import com.common.irendercore.dto.MyList;

import java.util.List;

public interface MyService<T> {

  T create(T t);

  void update(String id, T t);

  void delete(String id);

  T getById(String id);

  List<T> getByIds(List<String> ids);

  List<T> getAll();

  MyList<T> search(String filter, String sort, Integer pageIndex, Integer pageSize);
}
