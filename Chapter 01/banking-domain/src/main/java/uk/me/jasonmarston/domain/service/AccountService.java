package uk.me.jasonmarston.domain.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.aggregate.Account;
import uk.me.jasonmarston.domain.details.TransactionDetails;
import uk.me.jasonmarston.domain.details.TransactionIdentifierDetails;
import uk.me.jasonmarston.domain.entity.Transaction;
import uk.me.jasonmarston.domain.value.Balance;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface AccountService {
	Transaction depositFunds(
			@Valid final TransactionDetails transactionDetails);
	Account getAccount(@NotNull @Valid final EntityId id);
	Balance getBalance(@NotNull @Valid final EntityId id);
	Transaction getDeposit(
			@NotNull @Valid final TransactionIdentifierDetails 
					transactionIdentifierDetails);
	List<Transaction> getDeposits(@NotNull @Valid final EntityId accountId);
	Transaction getTransaction(
			@NotNull @Valid final TransactionIdentifierDetails
					transactionIdentifierDetails);
	List<Transaction> getTransactions(
			@NotNull @Valid final EntityId accountId);
	Transaction getWithdrawal(
			@NotNull @Valid final TransactionIdentifierDetails
					transactionIdentifierDetails);
	List<Transaction> getWithdrawals(@NotNull @Valid final EntityId accountId);
	Account openAccount();
	Transaction withdrawFunds(@NotNull @Valid final TransactionDetails
			transactionDetails);
}