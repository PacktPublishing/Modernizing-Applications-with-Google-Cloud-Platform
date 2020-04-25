package uk.me.jasonmarston.bean.impl;

import uk.me.jasonmarston.domain.aggregate.User;

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
