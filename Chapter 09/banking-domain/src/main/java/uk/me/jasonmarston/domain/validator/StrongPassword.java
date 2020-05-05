package uk.me.jasonmarston.domain.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import uk.me.jasonmarston.domain.validator.impl.StrongPasswordImpl;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordImpl.class)
public @interface StrongPassword {
	Class<?>[] groups() default {};

	String message() default "Weak Password";
	Class<?>[] payload() default {};
}