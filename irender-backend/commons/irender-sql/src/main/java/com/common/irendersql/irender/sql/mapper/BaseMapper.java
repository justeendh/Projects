package com.common.irendersql.irender.sql.mapper;

public interface BaseMapper<D, E> {
  D entityToDto(E entity);
  E dtoToEntity(D dto);
  D entityToDto(E entity, D dto);
  E dtoToEntity(D dto, E entity);
}
