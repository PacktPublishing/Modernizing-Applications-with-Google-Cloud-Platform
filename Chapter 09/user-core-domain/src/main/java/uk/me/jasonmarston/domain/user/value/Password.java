package uk.me.jasonmarston.domain.user.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

import uk.me.jasonmarston.domain.user.validator.StrongPassword;
import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@Embeddable
public class Password extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Password is required")
	@StrongPassword(message = "Invalid Password")
	@Column(columnDefinition = "CHAR(60)", nullable = false)
	private String password;

	private Password() {
	}

	public Password(final String password) {
		this();
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return password;
	}
}