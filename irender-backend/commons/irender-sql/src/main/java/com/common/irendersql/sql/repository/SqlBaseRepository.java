package com.common.irendersql.sql.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SqlBaseRepository<E> extends PagingAndSortingRepository<E, String>, JpaSpecificationExecutor<E> {

}
