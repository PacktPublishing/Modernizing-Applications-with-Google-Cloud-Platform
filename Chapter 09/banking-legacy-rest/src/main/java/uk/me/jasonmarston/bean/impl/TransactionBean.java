package uk.me.jasonmarston.bean.impl;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import uk.me.jasonmarston.domain.account.entity.Transaction;

public class TransactionBean {
	private ZonedDateTime dateTime;
	private String description;
	private String type;
	private BigDecimal amount;
	private boolean isCorrection;
	private UUID referenceAccountId;

	public TransactionBean() {
	}
	
	public TransactionBean(Transaction transaction) {
		dateTime = transaction.getDateTime();
		description = transaction.getDescription();
		type = transaction.getType().name();
		amount = transaction.getAmount().getAmount();
		isCorrection = transaction.isCorrection();
		referenceAccountId = transaction.getReferenceAccountId().getId();
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

	public UUID getReferenceAccountId() {
		return referenceAccountId;
	}

	public void setReferenceAccountId(UUID referenceAccountId) {
		this.referenceAccountId = referenceAccountId;
	}
}
