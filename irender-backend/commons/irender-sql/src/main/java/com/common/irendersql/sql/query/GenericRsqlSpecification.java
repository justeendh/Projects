package com.common.irendersql.sql.query;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenericRsqlSpecification<T> implements Specification<T> {
  private String property;
  private transient ComparisonOperator operator;
  private List<String> arguments;

  public GenericRsqlSpecification(final String property, final ComparisonOperator operator, final List<String> arguments) {
    super();
    this.property = property;
    this.operator = operator;
    this.arguments = arguments;
  }

  @Override
  public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
    final List<Object> args = castArguments(root);
    final Object argument = args.get(0);

    List<String> paths = Arrays.stream(property.split(".")).collect(Collectors.toList());
    Path path = root.get(paths.get(0));
    if (paths.size() > 1) {
      for (int i = 1; i < paths.size(); i++) {
        path = path.get(paths.get(i));
      }
    }

    switch (RsqlSearchOperation.getSimpleOperator(operator)) {

      case EQUAL:
        if (argument instanceof String) {
          return builder.like(path, argument.toString().replace('*', '%'));
        } else if (argument == null) {
          return builder.isNull(path);
        } else {
          return builder.equal(path, argument);
        }
      case NOT_EQUAL:
        if (argument instanceof String) {
          return builder.notLike(path, argument.toString().replace('*', '%'));
        } else if (argument == null) {
          return builder.isNotNull(path);
        } else {
          return builder.notEqual(path, argument);
        }
      case GREATER_THAN:
        return builder.greaterThan(path, argument.toString());
      case GREATER_THAN_OR_EQUAL:
        return builder.greaterThanOrEqualTo(path, argument.toString());
      case LESS_THAN:
        return builder.lessThan(path, argument.toString());
      case LESS_THAN_OR_EQUAL:
        return builder.lessThanOrEqualTo(path, argument.toString());
      case IN:
        return path.in(args);
      case NOT_IN:
        return builder.not(path.in(args));
        default:
    }

    return null;
  }

  // === private

  private List<Object> castArguments(final Path path) {

    final List<Object> args = new ArrayList<>();
    final Class<? extends Object> type = path.getJavaType();

    for (final String argument : arguments) {
      if (type.equals(Integer.class)) {
        args.add(Integer.parseInt(argument));
      } else if (type.equals(Long.class)) {
        args.add(Long.parseLong(argument));
      } else {
        args.add(argument);
      }
    }

    return args;
  }
}
