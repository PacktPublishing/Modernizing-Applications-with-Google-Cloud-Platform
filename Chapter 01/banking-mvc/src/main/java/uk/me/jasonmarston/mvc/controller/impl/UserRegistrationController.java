package uk.me.jasonmarston.mvc.controller.impl;

import static uk.me.jasonmarston.domain.Constants.STRONG_PASSWORD;

import java.util.Locale;

import javax.persistence.EntityExistsException;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.details.impl.RegistrationDetails;
import uk.me.jasonmarston.domain.factory.details.RegistrationDetailsBuilderFactory;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.service.VerificationTokenService;
import uk.me.jasonmarston.domain.value.impl.EmailAddress;
import uk.me.jasonmarston.domain.value.impl.Password;
import uk.me.jasonmarston.domain.value.impl.Token;
import uk.me.jasonmarston.mvc.alerts.impl.AlertDanger;
import uk.me.jasonmarston.mvc.alerts.impl.AlertInfo;
import uk.me.jasonmarston.mvc.controller.bean.impl.RegistrationBean;
import uk.me.jasonmarston.mvc.event.impl.OnRegistrationEvent;

@Controller
@Validated
public class UserRegistrationController {
	@Autowired
	@Lazy
	private VerificationTokenService verificationTokenService;

	@Autowired
	@Lazy
	private UserService userService;

	@Autowired
	@Lazy
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	@Lazy
	private RegistrationDetailsBuilderFactory 
			registrationDetailsBuilderfactory;

	@Autowired
	@Lazy
	private LocaleResolver localeResolver;

	private ModelAndView _expiredViewAndModel(final ModelAndView model) {
		model.addObject("strongPassword", STRONG_PASSWORD);
		model.addObject("registrationBean", new RegistrationBean());
		final AlertDanger alert = new AlertDanger("error.token");
		model.addObject("alert", alert);
		
		model.setViewName("user/registration");
		
		return model;
	}

	@GetMapping("/user/registration")
	public ModelAndView getUserRegistration() {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "register.heading");
		model.addObject("strongPassword", STRONG_PASSWORD);
		model.addObject("registrationBean", new RegistrationBean());
		
		model.setViewName("user/registration");

		return model;
	}

	@GetMapping("/user/registration/{token}")
	public ModelAndView getUserRegistrationToken(
			@PathVariable("token") final String tokenString) {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "register.heading");
		final Token token = new Token(tokenString);
		final VerificationToken verificationToken = 
				verificationTokenService.findByToken(token);
		if(verificationToken == null) {
			return _expiredViewAndModel(model);
		}

		verificationTokenService.delete(verificationToken.getId());

		if(verificationToken.isExpired()) {
			return _expiredViewAndModel(model);
		}

		User user = userService.findById(verificationToken.getUserId());
		if(user == null) {
			return _expiredViewAndModel(model);
		}

		try {
			user = userService.enable(user.getId());
		}
		catch(OptimisticLockException e) {
			return _expiredViewAndModel(model);
		}

		AuthenticationHelper.loginUser(user);

		final AlertInfo alert = new AlertInfo("info.emailVerified");
		model.addObject("alert", alert);

		model.setViewName("confirmation");

		return model;
	}

	@PostMapping("/user/registration")
	public ModelAndView postUserRegistration(
			@ModelAttribute("registrationBean") 
					@NotNull @Valid final RegistrationBean registrationBean,
					final HttpServletRequest request) {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "register.heading");
		try {
			final RegistrationDetails.Builder builder = 
					registrationDetailsBuilderfactory.create();
			final Locale locale = localeResolver.resolveLocale(request);

			final RegistrationDetails registrationDetails = builder
					.forEmail(new EmailAddress(registrationBean.getEmail()))
					.withPassword(new Password(registrationBean.getPassword()))
					.andPasswordConfirmation(
							new Password(
									registrationBean.getPasswordConfirmation()
							)
					)
					.inLocale(locale)
					.build();

			final User user = userService.register(registrationDetails);

			applicationEventPublisher
					.publishEvent(new OnRegistrationEvent(
							user, 
							request.getContextPath(),
							locale));
		}
		catch(final EntityExistsException e) {
			final AlertDanger alert = new AlertDanger("error.alreadyRegistered");
			model.addObject("alert", alert);
			model.addObject("strongPassword", STRONG_PASSWORD);
			model.addObject("registrationBean", registrationBean);

			model.setViewName("user/registration");

			return model;
		}

		final AlertInfo alert = new AlertInfo("info.registrationEmail");
		model.addObject("alert", alert);

		model.setViewName("confirmation");

		return model;
	}
}