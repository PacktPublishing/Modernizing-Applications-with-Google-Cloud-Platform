package uk.me.jasonmarston.mvc.event.impl;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import uk.me.jasonmarston.domain.aggregate.User;

public class OnRegistrationEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private User user;
	private String contextPath;
	private Locale locale;

	public OnRegistrationEvent(
			final User user,
			final String contextPath,
			final Locale locale) {
		super(user);
		if(user == null || contextPath == null || locale == null) {
			throw new IllegalArgumentException("Invalid OnRegistrationEvent");
		}

		this.user = user;
		this.contextPath = contextPath;
		this.locale = locale;
	}

	public String getContextPath() {
		return contextPath;
	}

	public Locale getLocale() {
		return locale;
	}
	
	public User getUser() {
		return user;
	}
}