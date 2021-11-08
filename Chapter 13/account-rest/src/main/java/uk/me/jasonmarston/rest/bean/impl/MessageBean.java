package uk.me.jasonmarston.rest.bean.impl;

import java.io.Serializable;

public class MessageBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public MessageBean() {
		this.message = "";
	}

	public MessageBean(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
