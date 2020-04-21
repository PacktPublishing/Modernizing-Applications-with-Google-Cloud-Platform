package uk.me.jasonmarston.domain.details;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.factory.details.RegistrationDetailsBuilderFactory;
import uk.me.jasonmarston.domain.validator.FieldsValueMatch;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.details.DetailsObject;
import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@FieldsValueMatch(
		field = "password", 
		fieldMatch = "passwordConfirmation", 
		message = "Passwords must match"
)
public class RegistrationDetails extends AbstractValueObject implements DetailsObject {
	public static class Builder implements IBuilder<RegistrationDetails> {
		private EmailAddress email;
		private Password password;
		private Password passwordConfirmation;
		private Locale locale;

		private Builder() {
		}

		public Builder andPasswordConfirmation(final Password passwordConfirmation) {
			this.passwordConfirmation = passwordConfirmation;
			return this;
		}

		@Override
		public RegistrationDetails build() {
			if(email == null || password == null || passwordConfirmation == null) {
				throw new IllegalArgumentException("Invalid registration details");
			}
			if(!password.equals(passwordConfirmation)) {
				throw new IllegalArgumentException("Passwords must match");
			}

			final RegistrationDetails registrationDetails = new RegistrationDetails();
			registrationDetails.email = email;
			registrationDetails.password = password;
			registrationDetails.passwordConfirmation = passwordConfirmation;
			registrationDetails.locale = locale;

			return registrationDetails;
		}

		public Builder forEmail(final EmailAddress email) {
			this.email = email;
			return this;
		}

		public Builder inLocale(final Locale locale) {
			this.locale = locale;
			return this;
		}

		public Builder withPassword(final Password password) {
			this.password = password;
			return this;
		}
	}

	@Service
	public static class Factory implements RegistrationDetailsBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Email is required")
	private EmailAddress email;

	@NotNull(message = "Password is required")
	private Password password;

	@NotNull(message = "Password confirmation is required")
	private Password passwordConfirmation;

	@NotNull(message = "Locale is required")
	private Locale locale;

	private RegistrationDetails() {
	}

	public EmailAddress getEmail() {
		return email;
	}

	public Locale getLocale() {
		return locale;
	}

	public Password getPassword() {
		return password;
	}

	public Password getPasswordConfirmation() {
		return passwordConfirmation;
	}
}