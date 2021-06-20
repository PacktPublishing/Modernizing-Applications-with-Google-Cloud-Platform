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

import uk.me.jasonmarston.domain.aggregate.impl.ResetToken;
import uk.me.jasonmarston.domain.service.ResetTokenService;
import uk.me.jasonmarston.mvc.event.impl.OnPasswordResetEvent;

@Component
public class PasswordResetListener implements
		ApplicationListener<OnPasswordResetEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetListener.class);

	@Autowired
	@Lazy
	private ResetTokenService resetTokenService;

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
	public void onApplicationEvent(final OnPasswordResetEvent event) {
		final ResetToken token = resetTokenService
				.create(event.getEmail());
		if(token == null) {
			LOGGER.warn("Email: "
					+ event.getEmail() 
					+ " does not match with a user.");
			return;
		}

		final MimeMessage message = sender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(event.getEmail().toString());
			helper.setFrom(from);

			final String subject = messageSource.getMessage(
					"templates.email.reset.subject",
					new Object[0],
					event.getLocale());
			helper.setSubject(subject);

			final Context context = new Context(event.getLocale());
			context.setVariable("host", hostName);
			context.setVariable("contextRoot", event.getContextPath());
			context.setVariable("token", token.getToken().toString());
			context.setVariable("locale", event.getLocale().toString());

			final String text = templateEngine.process(
					"emails/reset",
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