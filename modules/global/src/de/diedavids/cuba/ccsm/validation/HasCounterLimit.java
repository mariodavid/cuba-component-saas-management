package de.diedavids.cuba.ccsm.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = HasCounterLimitValidator.class)
public @interface HasCounterLimit {

    String message() default "{msg://de.diedavids.cuba.ccsm.validation/HasCounterLimit.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String limit();
}