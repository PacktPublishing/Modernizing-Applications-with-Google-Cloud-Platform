package uk.me.jasonmarston.mvc.event.listener.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.service.VerificationTokenService;
import uk.me.jasonmarston.mvc.event.impl.OnRegistrationEvent;

@Component
public class RegistrationListener implements
		ApplicationListener<OnRegistrationEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationListener.class);

	@Autowired
	@Lazy
	private VerificationTokenService verificationTokenService;

	@Autowired
	@Lazy
	private JavaMailSender sender;

	// Cannot be lazy due to final methods
	@Autowired
	@Qualifier("htmlEmailTemplateEngine")
	private TemplateEngine templateEngine;

	@Autowired
	@Lazy
	private MessageSource messageSource;

	@Value("${banking.email.from}")
    private String from;

	@Value("${banking.host.name}")
	private String hostName;

	@Async
	@Override
	public void onApplicationEvent(final OnRegistrationEvent event) {
		final User user = event.getUser();
		final VerificationToken token = verificationTokenService
				.create(user.getId());

		final MimeMessage message = sender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(user.getEmail());
			helper.setFrom(from);

			final String subject = messageSource.getMessage(
					"templates.email.registration.subject",
					new Object[0],
					event.getLocale());
			helper.setSubject(subject);

			final Context context = new Context(event.getLocale());
			context.setVariable("host", hostName);
			context.setVariable("contextRoot", event.getContextPath());
			context.setVariable("token", token.getToken().toString());
			context.setVariable("locale", event.getLocale().toString());

			final String text = templateEngine.process(
					"emails/register",
					context);
			helper.setText(text, true);

			sender.send(message);
		} catch (RuntimeException e ) {
			LOGGER.error("Failed to send confirmation email: " + e.getMessage());
		} catch (MessagingException e) {
			LOGGER.error("Failed to send confirmation email: " + e.getMessage());
		}
	}
}