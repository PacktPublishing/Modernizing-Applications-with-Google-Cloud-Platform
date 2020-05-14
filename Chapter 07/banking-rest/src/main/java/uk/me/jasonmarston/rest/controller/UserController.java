package uk.me.jasonmarston.rest.controller;

import org.springframework.http.ResponseEntity;

import uk.me.jasonmarston.domain.aggregate.User;

public interface UserController {
	ResponseEntity<?> getPreferences(final User user);
}
