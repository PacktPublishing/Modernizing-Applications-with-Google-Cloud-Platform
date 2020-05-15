package uk.me.jasonmarston.mvc.controller.impl;

import static uk.me.jasonmarston.domain.Constants.STRONG_PASSWORD;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.mvc.alerts.impl.AlertDanger;
import uk.me.jasonmarston.mvc.alerts.impl.AlertInfo;
import uk.me.jasonmarston.mvc.controller.bean.impl.ChangePasswordBean;

@Controller
@Validated
public class UserPasswordChangeController {
	@Autowired
	@Lazy
	private UserService userService;

	@GetMapping("/user/password/change")
	public ModelAndView getUserPasswordChange() {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "changePassword.heading");
		model.addObject("strongPassword", STRONG_PASSWORD);
		model.addObject("changePasswordBean", new ChangePasswordBean());
		
		model.setViewName("user/password/change");

		return model;
	}

	@PostMapping("/user/password/change")
	public ModelAndView postUserPasswordChange(
			@AuthenticationPrincipal final User user,
			@ModelAttribute("changePasswordBean") 
					@NotNull @Valid final ChangePasswordBean
							changePasswordBean,
			final WebRequest request) {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "changePassword.heading");

		if(!userService.isCurrentPassword(user.getId(), 
				new Password(changePasswordBean.getCurrentPassword()))) {
			final AlertDanger alert = new AlertDanger("error.password");
			model.addObject("strongPassword", STRONG_PASSWORD);
			model.addObject("changePasswordBean", changePasswordBean);
			model.addObject("alert", alert);

			model.setViewName("user/password/change");

			return model;
		}

		final User updatedUser = userService.changePassword(user.getId(),
				new Password(changePasswordBean.getPassword()));

		AuthenticationHelper.loginUser(updatedUser);

		final AlertInfo alert = new AlertInfo("info.passwordChanged");
		model.addObject("alert", alert);

		model.setViewName("confirmation");

		return model;
	}
}