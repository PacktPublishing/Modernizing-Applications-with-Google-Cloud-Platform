package uk.me.jasonmarston.mvc.controller.impl;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Validated
public class AccountController {
	@GetMapping({"/", "account"})
	public ModelAndView getAccount() {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "account.heading");

		model.setViewName("account/index");

		return model;
	}
}