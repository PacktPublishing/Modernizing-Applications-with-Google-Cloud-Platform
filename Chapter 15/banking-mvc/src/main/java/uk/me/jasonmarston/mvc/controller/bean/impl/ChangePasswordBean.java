package uk.me.jasonmarston.mvc.controller.bean.impl;

import javax.validation.constraints.NotBlank;

import uk.me.jasonmarston.domain.validator.FieldsValueMatch;
import uk.me.jasonmarston.domain.validator.StrongPassword;
import uk.me.jasonmarston.mvc.controller.bean.AbstractBean;

@FieldsValueMatch(
		field = "password", 
		fieldMatch = "passwordConfirmation", 
		message = "Passwords do not match!"
)
public class ChangePasswordBean extends AbstractBean {
	@NotBlank(message = "Current password is required")
	private String currentPassword;

	@NotBlank(message = "Password is required")
	@StrongPassword(message = "Invalid Password")
	private String password;

	@NotBlank(message = "Password confirmation is required")
	private String passwordConfirmation;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}