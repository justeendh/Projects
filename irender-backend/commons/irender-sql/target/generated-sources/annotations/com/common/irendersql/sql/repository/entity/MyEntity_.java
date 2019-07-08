package com.common.irendersql.sql.repository.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MyEntity.class)
public abstract class MyEntity_ {

	public static volatile SingularAttribute<MyEntity, Long> createdAt;
	public static volatile SingularAttribute<MyEntity, String> updatedBy;
	public static volatile SingularAttribute<MyEntity, String> createdBy;
	public static volatile SingularAttribute<MyEntity, String> id;
	public static volatile SingularAttribute<MyEntity, Long> updatedAt;

	public static final String CREATED_AT = "createdAt";
	public static final String UPDATED_BY = "updatedBy";
	public static final String CREATED_BY = "createdBy";
	public static final String ID = "id";
	public static final String UPDATED_AT = "updatedAt";

}

