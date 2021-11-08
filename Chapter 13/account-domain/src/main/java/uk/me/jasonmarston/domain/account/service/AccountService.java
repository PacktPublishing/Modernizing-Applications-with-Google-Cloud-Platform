package uk.me.jasonmarston.domain.account.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.account.aggregate.impl.Account;
import uk.me.jasonmarston.domain.account.details.impl.TransactionDetails;
import uk.me.jasonmarston.domain.account.details.impl.TransactionIdentifierDetails;
import uk.me.jasonmarston.domain.account.details.impl.TransferIdentifierDetails;
import uk.me.jasonmarston.domain.account.entity.impl.Transaction;
import uk.me.jasonmarston.domain.account.value.impl.Balance;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface AccountService {
	Transaction depositFunds(
			@NotNull @Valid final TransactionDetails transactionDetails);
	Account getAccount(@NotNull @Valid final EntityId id);
	List<Account> getAccounts(@NotNull @Valid final String ownerId);
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
	Transaction getTransfer(
			@NotNull @Valid final TransferIdentifierDetails
					transferIdentifierDetails);
	Transaction getWithdrawal(
			@NotNull @Valid final TransactionIdentifierDetails
					transactionIdentifierDetails);
	List<Transaction> getWithdrawals(@NotNull @Valid final EntityId accountId);
	Account openAccount(@NotNull @Valid final String ownerId, @NotEmpty final String name);
	Transaction withdrawFunds(@NotNull @Valid final TransactionDetails
			transactionDetails);
}