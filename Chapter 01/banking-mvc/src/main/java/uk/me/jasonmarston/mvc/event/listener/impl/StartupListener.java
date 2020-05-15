package uk.me.jasonmarston.mvc.event.listener.impl;

import java.util.Locale;

import javax.persistence.EntityExistsException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import uk.me.jasonmarston.domain.details.RegistrationDetails;
import uk.me.jasonmarston.domain.factory.details.RegistrationDetailsBuilderFactory;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;

// This is for development environments only.
// Would be a major security violation in production.
@Component
@Profile("!PRODUCTION")
public class StartupListener implements
		ApplicationListener<ApplicationReadyEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(StartupListener.class);
	@Autowired
	@Lazy
	private UserService userService;
	
	@Autowired
	@Lazy
	private RegistrationDetailsBuilderFactory
			registrationDetailsBuilderFactory;

	@Autowired
	@Lazy
	private ApplicationEventPublisher applicationEventPublisher;

	@Value("${banking.initial.admin.email}")
    private String emailString;

	@Value("${banking.initial.admin.password}")
	private String passwordString;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		if(StringUtils.isBlank(emailString) 
				|| StringUtils.isBlank(passwordString)) {
			return;
		}

		final EmailAddress email = new EmailAddress(emailString);
		final Password password = new Password(passwordString);

		if(userService.findByEmail(email) ==  null) {
			LOGGER.warn("Creating default admin account that is enabled and does not require email validation.");

			final RegistrationDetails.Builder builder = 
					registrationDetailsBuilderFactory.create();
			
			final RegistrationDetails details = builder
					.forEmail(email)
					.withPassword(password)
					.andPasswordConfirmation(password)
					.inLocale(Locale.forLanguageTag("en-UK"))
					.build();

			try {
				userService.registerAdministrator(details);
			}
			catch(final EntityExistsException e) {
				// assume multiple nodes starting and
				// trying to do this, so ignore as another one
				// has succeeded
				return;
			}
		}
	}
}
