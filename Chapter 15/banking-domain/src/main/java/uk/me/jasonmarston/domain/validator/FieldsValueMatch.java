package uk.me.jasonmarston.domain.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import uk.me.jasonmarston.domain.validator.impl.FieldsValueMatchValidator;

@Constraint(validatedBy = FieldsValueMatchValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsValueMatch {
	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@interface List {
		FieldsValueMatch[] value();
	}

	String field();
	String fieldMatch();

	Class<?>[] groups() default {};

	String message() default "Fields values don't match!";
	Class<?>[] payload() default {};
}