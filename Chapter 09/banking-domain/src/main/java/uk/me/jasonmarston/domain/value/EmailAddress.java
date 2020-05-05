package uk.me.jasonmarston.domain.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@Embeddable
public class EmailAddress extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Email is required")
	@Email(message = "Must be a valid email address")
	@Column(nullable = false, columnDefinition = "VARCHAR(250)")
	private String email;

	private EmailAddress() {
	}

	public EmailAddress(final String email) {
		this();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return email;
	}
}