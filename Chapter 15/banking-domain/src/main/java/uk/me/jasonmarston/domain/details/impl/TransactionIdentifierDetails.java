package uk.me.jasonmarston.domain.details.impl;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.factory.details.TransactionIdentifierDetailsBuilderFactory;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.details.DetailsObject;
import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class TransactionIdentifierDetails extends AbstractValueObject implements DetailsObject {
	public static class Builder implements IBuilder<TransactionIdentifierDetails> {
		private EntityId accountId;
		private EntityId transactionId;

		private Builder() {
		}

		@Override
		public TransactionIdentifierDetails build() {
			if(accountId == null || transactionId == null) {
				throw new IllegalArgumentException("Invalid transaction identifier details");
			}

			final TransactionIdentifierDetails transactionIdentifierDetails = 
					new TransactionIdentifierDetails();
			transactionIdentifierDetails.accountId = accountId;
			transactionIdentifierDetails.transactionId = transactionId;

			return transactionIdentifierDetails;
		}

		public Builder forAccountId(final EntityId accountId) {
			this.accountId = accountId;
			return this;
		}

		public Builder withTransactionId(final EntityId transactionId) {
			this.transactionId = transactionId;
			return this;
		}
	}

	@Service
	public static class Factory implements TransactionIdentifierDetailsBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Account Id is required")
	private EntityId accountId;

	@NotNull(message = "Transaction Id is required")
	private EntityId transactionId;

	private TransactionIdentifierDetails() {
	}

	public EntityId getAccountId() {
		return accountId;
	}

	public EntityId getTransactionId() {
		return transactionId;
	}
}