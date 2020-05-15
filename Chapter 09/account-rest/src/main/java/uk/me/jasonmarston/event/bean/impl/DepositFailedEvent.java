package uk.me.jasonmarston.event.bean.impl;

import java.math.BigDecimal;
import java.util.UUID;

import uk.me.jasonmarston.domain.account.details.TransactionDetails;

public class DepositFailedEvent {
	private UUID accountId;
	private UUID referenceAccountId;
	private String description;
	private UUID journalCode;
	private BigDecimal amount;
	private boolean isCorrection;
	
	public DepositFailedEvent() {
	}

	public DepositFailedEvent(TransactionDetails details) {
		accountId = details.getAccountId().getId();
		referenceAccountId = details.getReferenceAccountId().getId();
		description = details.getDescription();
		journalCode = details.getJournalCode().getId();
		amount = details.getAmount().getAmount();
		isCorrection = details.isCorrection();
	}

	public UUID getAccountId() {
		return accountId;
	}

	public void setAccountId(UUID accountId) {
		this.accountId = accountId;
	}

	public UUID getReferenceAccountId() {
		return referenceAccountId;
	}

	public void setReferenceAccountId(UUID referenceAccountId) {
		this.referenceAccountId = referenceAccountId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UUID getJournalCode() {
		return journalCode;
	}

	public void setJournalCode(UUID journalCode) {
		this.journalCode = journalCode;
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
