package uk.me.jasonmarston.rest.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.rest.bean.impl.UserBean;
import uk.me.jasonmarston.rest.controller.UserController;

@RestController
public class UserControllerImpl implements UserController {

    @Override
    @PreAuthorize("hasRole('USER')")
	@RequestMapping(path = "/user/preference",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getPreferences(
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(new UserBean(user));
	}
}
