package uk.me.jasonmarston.domain.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.value.impl.Token;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface VerificationTokenService {
	VerificationToken create(@NotNull @Valid final EntityId id);
	void delete(@NotNull @Valid final EntityId id);
	VerificationToken findByToken(
			@NotNull @Valid final Token token);
	List<VerificationToken> findExpiredTokens();
}