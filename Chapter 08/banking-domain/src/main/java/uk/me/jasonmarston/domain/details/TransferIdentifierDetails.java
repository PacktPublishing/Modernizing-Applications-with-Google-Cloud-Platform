package uk.me.jasonmarston.domain.details;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.factory.details.TransferIdentifierDetailsBuilderFactory;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.details.DetailsObject;
import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class TransferIdentifierDetails extends AbstractValueObject implements DetailsObject {
	public static class Builder implements IBuilder<TransferIdentifierDetails> {
		private EntityId accountId;
		private EntityId journalCode;
		private boolean isCorrection;

		private Builder() {
		}

		@Override
		public TransferIdentifierDetails build() {
			if(accountId == null || journalCode == null) {
				throw new IllegalArgumentException("Invalid transfer identifier details");
			}

			final TransferIdentifierDetails transferIdentifierDetails = 
					new TransferIdentifierDetails();
			transferIdentifierDetails.journalCode = journalCode;
			transferIdentifierDetails.isCorrection = isCorrection;
			transferIdentifierDetails.accountId = accountId;

			return transferIdentifierDetails;
		}

		public Builder forAccountId(final EntityId accountId) {
			this.accountId = accountId;
			return this;
		}

		public Builder forJournalCode(final EntityId journalCode) {
			this.journalCode = journalCode;
			return this;
		}

		public Builder asCorrection(final boolean isCorrection) {
			this.isCorrection = isCorrection;
			return this;
		}
	}

	@Service
	public static class Factory implements TransferIdentifierDetailsBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Journal Code is required")
	private EntityId journalCode;

	@NotNull(message = "Account ID is required")
	private EntityId accountId;

	private boolean isCorrection;

	private TransferIdentifierDetails() {
	}

	public EntityId getJournalCode() {
		return journalCode;
	}

	public boolean isCorrection() {
		return isCorrection;
	}

	public EntityId getAccountId() {
		return accountId;
	}
}
