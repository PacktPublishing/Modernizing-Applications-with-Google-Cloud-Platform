package uk.me.jasonmarston.domain.value.impl;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@Embeddable
public class Token extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Token is required")
	@Column(nullable = false)
	private String token;

	private Token() {
	}

	public Token(final String token) {
		this();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		return token;
	}
}