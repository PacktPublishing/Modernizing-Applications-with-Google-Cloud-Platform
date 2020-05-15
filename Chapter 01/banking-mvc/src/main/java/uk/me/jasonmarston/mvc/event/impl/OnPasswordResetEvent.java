package uk.me.jasonmarston.mvc.event.impl;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import uk.me.jasonmarston.domain.value.EmailAddress;

public class OnPasswordResetEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private EmailAddress email;
	private String contextPath;
	private Locale locale;

	public OnPasswordResetEvent(
			final EmailAddress email,
			final String contextPath,
			final Locale locale) {
		super(email);
		if(email == null || contextPath == null || locale == null) {
			throw new IllegalArgumentException("Invalid OnPasswordResetEvent");
		}

		this.email = email;
		this.contextPath = contextPath;
		this.locale = locale;
	}

	public String getContextPath() {
		return contextPath;
	}

	public EmailAddress getEmail() {
		return email;
	}

	public Locale getLocale() {
		return locale;
	}
}