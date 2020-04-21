package uk.me.jasonmarston.rest.controller.command.impl;

import java.io.Serializable;

import uk.me.jasonmarston.domain.value.Amount;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class Transfer implements Serializable {
	private static final long serialVersionUID = 1L;
	private EntityId fromAccount;
	private EntityId toAccount;
	private Amount amount;

	public Transfer() {
	}

	public Transfer(final EntityId fromAccount,
			final EntityId toAccount,
			final Amount amount) {
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
	}

	public EntityId getFromAccount() {
		return fromAccount;
	}

	public EntityId getToAccount() {
		return toAccount;
	}

	public Amount getAmount() {
		return amount;
	}
}
