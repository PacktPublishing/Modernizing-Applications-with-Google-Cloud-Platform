package uk.me.jasonmarston.rest.bean.impl;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public Message() {
		this.message = "";
	}

	public Message(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
