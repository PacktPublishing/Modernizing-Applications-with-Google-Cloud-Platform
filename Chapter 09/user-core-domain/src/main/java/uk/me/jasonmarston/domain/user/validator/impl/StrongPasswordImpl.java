package uk.me.jasonmarston.domain.user.validator.impl;

import static uk.me.jasonmarston.domain.user.Constants.STRONG_PASSWORD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import uk.me.jasonmarston.domain.user.validator.StrongPassword;

public class StrongPasswordImpl implements ConstraintValidator<StrongPassword, String> {
	@Override
	public boolean isValid(
			final String value,
			final ConstraintValidatorContext context) {
		if(StringUtils.isBlank(value)) {
			return true;
		}

		final Pattern pattern = Pattern.compile(STRONG_PASSWORD);

		final Matcher matcher = pattern.matcher(value);
		if(matcher.matches()) {
			return true;
		}

		return false;
	}
}