package uk.me.jasonmarston.mvc.controller.bean;

import javax.validation.constraints.NotBlank;

import uk.me.jasonmarston.domain.validator.FieldsValueMatch;
import uk.me.jasonmarston.domain.validator.StrongPassword;

@FieldsValueMatch(
		field = "password", 
		fieldMatch = "passwordConfirmation", 
		message = "Passwords do not match!"
)
public class ResetPasswordBean extends AbstractBean {
	@NotBlank(message = "Password is required")
	@StrongPassword(message = "Invalid Password")
	private String password;

	@NotBlank(message = "Password confirmation is required")
	private String passwordConfirmation;

	public String getPassword() {
		return password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}