package uk.me.jasonmarston.rest.bean.impl;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class WithdrawalBean {
	@NotEmpty
	private String description;
	@NotNull
	@DecimalMin(value = "0.01", inclusive = true)
	@DecimalMax(value = "999999.99", inclusive = true)
	@Digits(integer = 7, fraction = 2)
	private BigDecimal amount;
	private UUID otherAccount;

	public WithdrawalBean() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public UUID getOtherAccount() {
		return otherAccount;
	}

	public void setOtherAccount(UUID otherAccount) {
		this.otherAccount = otherAccount;
	}
}
