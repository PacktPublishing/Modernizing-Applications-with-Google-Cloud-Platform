package uk.me.jasonmarston.domain.auth.service;

import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.user.aggregate.User;
import uk.me.jasonmarston.framework.authentication.impl.Token;

public interface AuthService {
	User sync(@NotNull Token token);
}