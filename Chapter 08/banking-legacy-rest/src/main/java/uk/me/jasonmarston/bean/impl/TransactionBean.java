package uk.me.jasonmarston.bean.impl;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import uk.me.jasonmarston.domain.entity.Transaction;

public class TransactionBean {
	private ZonedDateTime dateTime;
	private String description;
	private String type;
	private BigDecimal amount;
	private boolean isCorrection;

	public TransactionBean() {
	}
	
	public TransactionBean(Transaction transaction) {
		dateTime = transaction.getDateTime();
		description = transaction.getDescription();
		type = transaction.getType().name();
		amount = transaction.getAmount().getAmount();
		isCorrection = transaction.isCorrection();
	}

	public ZonedDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(ZonedDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean isCorrection() {
		return isCorrection;
	}

	public void setCorrection(boolean isCorrection) {
		this.isCorrection = isCorrection;
	}
}
