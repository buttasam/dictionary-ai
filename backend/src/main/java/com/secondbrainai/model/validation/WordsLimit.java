package com.secondbrainai.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = WordsLimit.WordsLimitValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface WordsLimit {

    String message() default "Limit of words exceeded";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int limit() default 3;

    class WordsLimitValidator implements ConstraintValidator<WordsLimit, String> {

        private int limit;

        @Override
        public void initialize(WordsLimit constraintAnnotation) {
            limit = constraintAnnotation.limit();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value.split(" ").length < limit;
        }
    }


}
