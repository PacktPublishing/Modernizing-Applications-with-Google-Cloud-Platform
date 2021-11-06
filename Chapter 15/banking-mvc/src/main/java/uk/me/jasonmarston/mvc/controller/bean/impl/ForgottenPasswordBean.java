package uk.me.jasonmarston.mvc.controller.bean.impl;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import uk.me.jasonmarston.mvc.controller.bean.AbstractBean;

public class ForgottenPasswordBean extends AbstractBean {
	@NotBlank(message = "Email address is required")
	@Email(message = "Must be a valid email address")
	private String email;

	public ForgottenPasswordBean() {
	}

	public ForgottenPasswordBean(final String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}