package uk.me.jasonmarston.rest.bean.impl;

import java.math.BigDecimal;
import java.util.UUID;

import uk.me.jasonmarston.domain.account.aggregate.impl.Account;

public class AccountBean {
	private String ownerId;
	private UUID id;
	private String name;
	private BigDecimal balance;
	
	public AccountBean() {
	}
	
	public AccountBean(Account account) {
		ownerId = account.getOwnerId();
		id = account.getId().getId();
		name = account.getName();
		balance = account.getBalance().getBalance();
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
