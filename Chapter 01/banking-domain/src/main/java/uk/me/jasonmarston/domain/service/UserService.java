package uk.me.jasonmarston.domain.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.details.RegistrationDetails;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.framework.authentication.impl.Token;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface UserService {
	User addAuthority(@NotNull @Valid final EntityId id,
			@NotNull final GrantedAuthority authority);
	User changePassword(
			@NotNull @Valid final EntityId id,
			@NotNull @Valid final Password password);
	void delete(@NotNull @Valid final EntityId id);
	User enable(@NotNull @Valid final EntityId id);
	User findByEmail(@NotNull @Valid final EmailAddress email);
	User findById(@NotNull @Valid final EntityId id);
	boolean isCurrentPassword(@NotNull @Valid final EntityId id,
			@NotNull final Password password);
	boolean login(@NotNull @Valid final EntityId id,
			@NotNull final Password password);
	User register(
			@NotNull @Valid final RegistrationDetails registrationDetails);
	User registerAdministrator(
			@NotNull @Valid final RegistrationDetails registrationDetails);
	User sync(@NotNull Token token);
}