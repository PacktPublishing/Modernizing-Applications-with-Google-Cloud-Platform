package uk.me.jasonmarston.mvc.controller.bean.impl;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.validator.FieldsValueMatch;
import uk.me.jasonmarston.domain.validator.StrongPassword;
import uk.me.jasonmarston.mvc.controller.bean.AbstractBean;

@FieldsValueMatch(
		field = "password", 
		fieldMatch = "passwordConfirmation", 
		message = "Passwords must match"
)
public class RegistrationBean extends AbstractBean {
	@NotNull(message = "Email address is required")
	@Email(message = "Must be a valid email address")
	private String email;

	@NotBlank(message = "Password is required")
	@StrongPassword(message = "Invalid Password!!!")
	private String password;

	@NotBlank(message = "Password confirmation is required")
	private String passwordConfirmation;

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}