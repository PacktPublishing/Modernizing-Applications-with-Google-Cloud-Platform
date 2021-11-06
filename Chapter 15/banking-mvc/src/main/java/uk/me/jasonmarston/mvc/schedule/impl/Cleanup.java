package uk.me.jasonmarston.mvc.schedule.impl;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.me.jasonmarston.domain.aggregate.impl.ResetToken;
import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.service.ResetTokenService;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.service.VerificationTokenService;

@Component
@Transactional(propagation = Propagation.REQUIRED, 
		isolation = Isolation.READ_COMMITTED, 
		readOnly = false)
public class Cleanup {
	private static final int ONE_MINUTE = 60000;
	private static final int THIRTY_SECONDS = 30000;

	@Autowired
	private VerificationTokenService verificationTokenService;

	@Autowired
	private ResetTokenService resetTokenService;
	
	@Autowired
	private UserService userService;

	private int _forUpToTenSeconds() {
		final Random random = new SecureRandom();
		return random.nextInt(11);

	}

	@Scheduled(initialDelay = THIRTY_SECONDS, fixedDelay = ONE_MINUTE)
	public void cleanupResetTokens() {
		try {
			TimeUnit.SECONDS.sleep(_forUpToTenSeconds());
		} catch (InterruptedException e) {
			return;
		}
		final List<ResetToken> list = resetTokenService.findExpiredTokens();
		for(ResetToken token: list) {
			resetTokenService.delete(token.getId());
		}
	}

	@Scheduled(fixedDelay = ONE_MINUTE)
	public void cleanupVerificationTokens() {
		try {
			TimeUnit.SECONDS.sleep(_forUpToTenSeconds());
		} catch (InterruptedException e) {
			return;
		}
		final List<VerificationToken> list = verificationTokenService
				.findExpiredTokens();
		for(VerificationToken token: list) {
			verificationTokenService.delete(token.getId());
			final User user = userService.findById(token.getUserId());
			if(!user.isEnabled()) {
				userService.delete(user.getId());
			}
		}
	}
}