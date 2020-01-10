package uk.me.jasonmarston.domain.details;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.factory.details.TransferDetailsBuilderFactory;
import uk.me.jasonmarston.domain.value.Amount;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.details.DetailsObject;
import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class TransferDetails extends AbstractValueObject implements DetailsObject {
	public static class Builder implements IBuilder<TransferDetails> {
		private EntityId fromAccountId;
		private EntityId toAccountId;
		private Amount amount;

		private Builder() {
		}

		@Override
		public TransferDetails build() {
			if(fromAccountId == null || toAccountId == null || amount == null) {
				throw new IllegalArgumentException("Invalid transfer details");
			}

			final TransferDetails transferDetails = new TransferDetails();
			transferDetails.fromAccountId = fromAccountId;
			transferDetails.toAccountId = toAccountId;
			transferDetails.amount = amount;

			return transferDetails;
		}

		public Builder forAmount(final Amount amount) {
			this.amount = amount;
			return this;
		}

		public Builder fromAccountId(final EntityId fromAccountId) {
			this.fromAccountId = fromAccountId;
			return this;
		}

		public Builder toAccountId(final EntityId toAccountId) {
			this.toAccountId = toAccountId;
			return this;
		}
	}

	@Service
	public static class Factory implements TransferDetailsBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@NotNull(message = "From Account Id is required")
	private EntityId fromAccountId;

	@NotNull(message = "To Account Id is required")
	private EntityId toAccountId;

	@NotNull(message = "Amount is required")
	private Amount amount;

	private TransferDetails() {
	}

	public Amount getAmount() {
		return amount;
	}

	public EntityId getFromAccountId() {
		return fromAccountId;
	}

	public EntityId getToAccountId() {
		return toAccountId;
	}
}