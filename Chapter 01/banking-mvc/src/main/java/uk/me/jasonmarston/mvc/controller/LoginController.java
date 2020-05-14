package uk.me.jasonmarston.mvc.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uk.me.jasonmarston.mvc.alerts.AlertDanger;
import uk.me.jasonmarston.mvc.alerts.AlertInfo;

@Controller
public class LoginController {
	private String _getErrorKey(HttpServletRequest request) {
		final Exception e = (Exception)request
				.getSession()
				.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		if(e == null) {
			return null;
		}

		if(e instanceof UsernameNotFoundException) {
			return "error.user.not.found";
		}

		if(e instanceof DisabledException) {
			return "error.user.disabled";
		}

		if(e instanceof LockedException) {
			return "error.user.locked";
		}

		if(e instanceof AccountExpiredException) {
			return "error.user.account.expired";
		}

		if(e instanceof CredentialsExpiredException) {
			return "error.user.credentials.expired";
		}

		if(e instanceof BadCredentialsException) {
			return "error.user.credentials.bad";
		}

		return null;
	}

	@GetMapping("/login")
	public ModelAndView getLogin(
			final @RequestParam(value ="error", required = false) 
					String error,
			final @RequestParam(value = "logout", required = false)
					String logout,
			final HttpServletRequest request) {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "login.heading");

		if(error != null) {
			final String errorKey = _getErrorKey(request);
			if(errorKey != null) {
				final AlertDanger alert = new AlertDanger(errorKey);
				model.addObject("alert", alert);
			}
		}
		else if(logout != null) {
			final AlertInfo alert = new AlertInfo("info.logout");
			model.addObject("alert", alert);
		}
		
		model.setViewName("login");
		
		return model;
	}
}