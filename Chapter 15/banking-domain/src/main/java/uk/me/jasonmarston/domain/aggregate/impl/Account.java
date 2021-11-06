package uk.me.jasonmarston.domain.aggregate.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.entity.impl.Transaction;
import uk.me.jasonmarston.domain.factory.aggregate.AccountBuilderFactory;
import uk.me.jasonmarston.domain.factory.entity.TransactionBuilderFactory;
import uk.me.jasonmarston.domain.value.impl.Amount;
import uk.me.jasonmarston.domain.value.impl.Balance;
import uk.me.jasonmarston.domain.value.impl.TransactionType;
import uk.me.jasonmarston.framework.domain.aggregate.AbstractAggregate;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Entity
@Table(name = "ACCOUNTS")
public class Account extends AbstractAggregate {
	public static class Builder implements IBuilder<Account> {
		private Balance balance;
		private String ownerId;
		private String name;

		private Builder() {
		}

		@Override
		public Account build() {
			if(balance == null || ownerId == null || name == null) {
				throw new IllegalArgumentException("Invalid account");
			}

			final Account account = new Account();
			account.balance = balance;
			account.ownerId = ownerId;
			account.name = name;

			return account;
		}

		public Builder forOwner(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder withOpeningBalance(Balance balance) {
			this.balance = balance;
			return this;
		}
	}

	@Service
	public static class Factory implements AccountBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@NotNull
	@Valid
	private Balance balance;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String ownerId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account", fetch = FetchType.EAGER)
	@NotNull
	private List<@NotNull Transaction> transactions = 
			new ArrayList<Transaction>();

	private Account() {
		super();
	}

	@Override
	protected String[] _getExcludeFromUniqueness() {
		return new String[] { "transactions" };
	}

	public Transaction depositFunds(final Amount amount, 
			final String description,
			final EntityId referenceAccountId,
			final boolean isCorrection) {
		balance = balance.add(amount);

		final Transaction.Builder builder = 
				_getBean(TransactionBuilderFactory.class).create();
		final Transaction transaction = builder
				.againstAccount(this)
				.ofType(TransactionType.DEPOSIT)
				.forAmount(amount)
				.withDescrption(description)
				.withReferenceAccountId(referenceAccountId)
				.asCorrection(isCorrection)
				.build();

		transactions.add(transaction);

		return transaction;
	}

	public Balance getBalance() {
		return balance;
	}

	public List<Transaction> getDeposits() {
		return transactions
			.stream()
			.filter(transaction -> 
				TransactionType.DEPOSIT.equals(transaction.getType()))
			.collect(Collectors.toList());
	}
	
	public String getName() {
		return name;
	}
	
	public String getOwnerId() {
		return ownerId;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public List<Transaction> getWithdrawals() {
		return transactions
			.stream()
			.filter(transaction -> 
				TransactionType.WITHDRAWAL.equals(transaction.getType()))
			.collect(Collectors.toList());
	}

	public Transaction withdrawFunds(final Amount amount,
			final String description,
			final EntityId referenceAccountId) {
		balance = balance.subtract(amount);

		final Transaction.Builder builder = 
				_getBean(TransactionBuilderFactory.class).create();
		final Transaction transaction = builder
				.againstAccount(this)
				.ofType(TransactionType.WITHDRAWAL)
				.forAmount(amount)
				.withDescrption(description)
				.withReferenceAccountId(referenceAccountId)
				.build();

		transactions.add(transaction);

		return transaction;
	}
}