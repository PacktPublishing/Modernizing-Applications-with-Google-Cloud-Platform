package uk.me.jasonmarston.rest.bean.impl;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DepositBean {
	@NotEmpty
	private String description;
	@NotNull
	@DecimalMin(value = "0.01", inclusive = true)
	@DecimalMax(value = "999999.99", inclusive = true)
	@Digits(integer = 7, fraction = 2)
	private BigDecimal amount;

	public DepositBean() {
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
}
