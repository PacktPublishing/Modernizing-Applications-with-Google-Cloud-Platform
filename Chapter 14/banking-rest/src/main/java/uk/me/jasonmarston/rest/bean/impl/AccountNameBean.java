package uk.me.jasonmarston.rest.bean.impl;

import javax.validation.constraints.NotEmpty;

public class AccountNameBean {
	
	@NotEmpty
	private String name;
	
	public AccountNameBean() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
