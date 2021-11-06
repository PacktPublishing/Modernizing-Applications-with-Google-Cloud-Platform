package uk.me.jasonmarston.mvc.controller.impl;

import static uk.me.jasonmarston.domain.Constants.STRONG_PASSWORD;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import uk.me.jasonmarston.domain.aggregate.impl.ResetToken;
import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.service.ResetTokenService;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.value.impl.EmailAddress;
import uk.me.jasonmarston.domain.value.impl.Password;
import uk.me.jasonmarston.domain.value.impl.Token;
import uk.me.jasonmarston.mvc.alerts.impl.AlertDanger;
import uk.me.jasonmarston.mvc.alerts.impl.AlertInfo;
import uk.me.jasonmarston.mvc.controller.bean.impl.ForgottenPasswordBean;
import uk.me.jasonmarston.mvc.controller.bean.impl.ResetPasswordBean;
import uk.me.jasonmarston.mvc.event.impl.OnPasswordResetEvent;

@Controller
@Validated
public class UserPasswordResetController {
	@Autowired
	@Lazy
	private ResetTokenService resetTokenService;

	@Autowired
	@Lazy
	private UserService userService;

	@Autowired
	@Lazy
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	@Lazy
	private LocaleResolver localeResolver;

	private ModelAndView _expiredViewAndModel(final ModelAndView model) {
		model.addObject("forgottenPasswordBean", new ForgottenPasswordBean());
		final AlertDanger alert = new AlertDanger("error.token");
		model.addObject("alert", alert);
		
		model.setViewName("user/password/reset");
		
		return model;
	}

	@GetMapping("/user/password/reset")
	public ModelAndView getUserPasswordReset() {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "resetPassword.heading");
		model.addObject("forgottenPasswordBean", new ForgottenPasswordBean());
		
		model.setViewName("user/password/reset");

		return model;
	}

	@GetMapping("/user/password/reset/{token}")
	public ModelAndView getUserPasswordResetToken(
			final WebRequest request, 
			@PathVariable("token") final String tokenString) {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "resetPassword.heading");

		final Token token =  new Token(tokenString);
		final ResetToken resetToken = 
				resetTokenService.findByToken(token);
		if(resetToken == null || resetToken.isExpired()) {
			return _expiredViewAndModel(model);
		}

		final User user = userService.findById(resetToken.getUserId());
		if(user == null) {
			return _expiredViewAndModel(model);
		}

		//model.addObject("token", token);
		model.addObject("strongPassword", STRONG_PASSWORD);
		model.addObject("resetPasswordBean", new ResetPasswordBean());
		
		model.setViewName("user/password/reset/verification");

	    return model;
	}

	@PostMapping("/user/password/reset")
	public ModelAndView postUserPasswordReset(
			@ModelAttribute("forgottenPasswordBean") 
					@NotNull @Valid final ForgottenPasswordBean 
							forgottenPasswordBean,
			final HttpServletRequest request) {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "resetPassword.heading");
		final AlertInfo alert = new AlertInfo("info.passwordResetEmail");
		model.addObject("alert", alert);

		applicationEventPublisher
				.publishEvent(new OnPasswordResetEvent(
						new EmailAddress(forgottenPasswordBean.getEmail()),
						request.getContextPath(),
						localeResolver.resolveLocale(request)));

		model.setViewName("confirmation");

		return model;
	}

	@PostMapping("/user/password/reset/{token}")
	public ModelAndView postUserPasswordResetToken(
			@PathVariable("token") String tokenString,
			@ModelAttribute("resetPasswordBean") 
					@NotNull @Valid final ResetPasswordBean resetPasswordBean,
			final ModelMap modelSession) {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "resetPassword.heading");

		final Token token = new Token(tokenString);

		final ResetToken resetToken = resetTokenService
				.findByToken(token);
		if(resetToken == null) {
			return _expiredViewAndModel(model);
		}

		resetTokenService.delete(resetToken.getId());

		if(resetToken.isExpired()) {
			return _expiredViewAndModel(model);
		}

		User user = userService.findById(resetToken.getUserId());
		if(user == null) {
			return _expiredViewAndModel(model);
		}

		try {
			user = userService.changePassword(user.getId(), 
					new Password(resetPasswordBean.getPassword()));
		}
		catch(OptimisticLockException e) {
			return _expiredViewAndModel(model);
		}

		AuthenticationHelper.loginUser(user);

		final AlertInfo alert = new AlertInfo("info.passwordReset");
		model.addObject("alert", alert);
		
		model.setViewName("confirmation");

	    return model;
	}
}