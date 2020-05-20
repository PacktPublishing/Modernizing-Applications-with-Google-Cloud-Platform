package uk.me.jasonmarston.rest.bean.impl;

import uk.me.jasonmarston.domain.user.aggregate.impl.User;

public class UserBean {
	private String locale;
	
	public UserBean() {
	}

	public UserBean(final User user) {
		this.locale = user.getLocale().toString();
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}
