package com.common.irenderqueue.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {EmailListValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface EmailList {
  String message() default "Invalid Email Address";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
