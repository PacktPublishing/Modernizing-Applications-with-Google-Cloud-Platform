package uk.me.jasonmarston.domain.validator.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import uk.me.jasonmarston.domain.validator.FieldsValueMatch;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {
	private String field;
	private String fieldMatch;
 
	public void initialize(FieldsValueMatch constraintAnnotation) {
		this.field = constraintAnnotation.field();
		this.fieldMatch = constraintAnnotation.fieldMatch();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		final Object fieldValue = new BeanWrapperImpl(value)
				.getPropertyValue(field);

		final Object fieldMatchValue = new BeanWrapperImpl(value)
				.getPropertyValue(fieldMatch);

		if (fieldValue != null) {
			if(fieldValue.equals(fieldMatchValue)) {
				return true;
			}

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					context.getDefaultConstraintMessageTemplate())
				.addNode(fieldMatch)
				.addConstraintViolation();

			return false;
    	} else {
    		if(fieldMatchValue == null) {
    			return true;
    		}

    		context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					context.getDefaultConstraintMessageTemplate())
				.addNode(fieldMatch)
				.addConstraintViolation();

			return false;
    	}
    }
}