package uk.me.jasonmarston.domain.details;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.factory.details.TransactionDetailsBuilderFactory;
import uk.me.jasonmarston.domain.value.Amount;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.details.DetailsObject;
import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class TransactionDetails extends AbstractValueObject implements DetailsObject {
	public static class Builder implements IBuilder<TransactionDetails> {
		private EntityId accountId;
		private Amount amount;
		private String description;

		private Builder() {
		}

		@Override
		public TransactionDetails build() {
			if(accountId == null || amount == null) {
				throw new IllegalArgumentException("Invalid transaction details");
			}

			final TransactionDetails transactionDetails = 
					new TransactionDetails();
			transactionDetails.accountId = accountId;
			transactionDetails.amount = amount;
			transactionDetails.description = description;

			return transactionDetails;
		}

		public Builder forAccountId(final EntityId accountId) {
			this.accountId = accountId;
			return this;
		}

		public Builder withAmount(final Amount amount) {
			this.amount = amount;
			return this;
		}
		
		public Builder withDescription(final String description) {
			this.description = description;
			return this;
		}
	}

	@Service
	public static class Factory implements TransactionDetailsBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Account Id is required")
	private EntityId accountId;

	@NotNull(message = "Amount is required")
	@Valid
	private Amount amount;
	
	@NotEmpty
	private String description;

	private TransactionDetails() {
	}

	public TransactionDetails(final EntityId accountId, final Amount amount) {
		this.accountId = accountId;
		this.amount = amount;
	}

	public EntityId getAccountId() {
		return accountId;
	}

	public Amount getAmount() {
		return amount;
	}
	
	public String getDescription() {
		return description;
	}
}