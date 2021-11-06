package uk.me.jasonmarston.domain.service.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.factory.aggregate.VerificationTokenBuilderFactory;
import uk.me.jasonmarston.domain.repository.VerificationTokenRepository;
import uk.me.jasonmarston.domain.repository.specification.impl.VerificationTokenSpecification;
import uk.me.jasonmarston.domain.service.VerificationTokenService;
import uk.me.jasonmarston.domain.value.impl.Token;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Service
@Validated
@Transactional(propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ, 
		readOnly = false)
public class VerificationTokenServiceImpl implements VerificationTokenService {
	@Autowired
	@Lazy
	private VerificationTokenBuilderFactory verificationTokenBuilderFactory;

	@Autowired
	@Lazy
	private VerificationTokenRepository verificationTokenRepository;

	@Override
	public VerificationToken create(@NotNull @Valid final EntityId id) {
		final VerificationToken.Builder builder = 
				verificationTokenBuilderFactory.create();

		final VerificationToken token = builder
				.forUserId(id)
				.build();

		return verificationTokenRepository.save(token);
	}

	@Override
	public void delete(@NotNull @Valid EntityId id) {
		verificationTokenRepository.deleteById(id);
	}

	@Override
	public VerificationToken findByToken(
			@NotNull @Valid Token token) {
		final Optional<VerificationToken> optional = 
				verificationTokenRepository
						.findByToken(token);
		if(optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public List<VerificationToken> findExpiredTokens() {
		return verificationTokenRepository
				.findAll(
						new VerificationTokenSpecification
								.VerificationTokenIsExpired());
	}
}