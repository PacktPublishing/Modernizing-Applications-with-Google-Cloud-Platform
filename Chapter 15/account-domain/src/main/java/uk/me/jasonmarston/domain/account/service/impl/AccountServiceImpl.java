package uk.me.jasonmarston.domain.account.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.account.aggregate.impl.Account;
import uk.me.jasonmarston.domain.account.details.impl.TransactionDetails;
import uk.me.jasonmarston.domain.account.details.impl.TransactionIdentifierDetails;
import uk.me.jasonmarston.domain.account.details.impl.TransferIdentifierDetails;
import uk.me.jasonmarston.domain.account.entity.impl.Transaction;
import uk.me.jasonmarston.domain.account.factory.aggregate.AccountBuilderFactory;
import uk.me.jasonmarston.domain.account.repository.AccountRepository;
import uk.me.jasonmarston.domain.account.repository.TransactionRepository;
import uk.me.jasonmarston.domain.account.repository.specification.impl.TransactionSpecification.DepositHasIdAndAccountId;
import uk.me.jasonmarston.domain.account.repository.specification.impl.TransactionSpecification.TransactionHasIdAndAccountId;
import uk.me.jasonmarston.domain.account.repository.specification.impl.TransactionSpecification.WithdrawalHasIdAndAccountId;
import uk.me.jasonmarston.domain.account.service.AccountService;
import uk.me.jasonmarston.domain.account.value.impl.Balance;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Service
@Validated
@Transactional(propagation = Propagation.REQUIRED, 
		isolation = Isolation.REPEATABLE_READ, 
		readOnly = false)
public class AccountServiceImpl implements AccountService {

	@Autowired
	@Lazy
	private AccountBuilderFactory accountBuilderFactory;

	@Autowired
	@Lazy
	private AccountRepository accountRepository;

	@Autowired
	@Lazy
	private TransactionRepository transactionRepository;

	@Override
	public Transaction depositFunds(
			@NotNull @Valid final TransactionDetails transactionDetails) {
		final Optional<Account> optional = accountRepository
				.findById(transactionDetails.getAccountId());
		if(optional.isPresent()) {
			final Account account = optional.get();
			final Transaction transaction = account
					.depositFunds(transactionDetails.getAmount(),
							transactionDetails.getDescription(),
							transactionDetails.getReferenceAccountId(),
							transactionDetails.isCorrection());

			accountRepository.save(account);

			return transaction;
		}

		return null;
	}

	@Override
	public Account getAccount(@NotNull @Valid final EntityId id) {
		final Optional<Account> optional = accountRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public List<Account> getAccounts(@NotNull @Valid String ownerId) {
		return accountRepository.findByOwnerId(ownerId);
	}

	@Override
	public Balance getBalance(@NotNull @Valid final EntityId id) {
		final Optional<Account> optional = accountRepository.findById(id);
		if(optional.isPresent()) {
			final Account account = optional.get();
			return account.getBalance();
		}

		return null;
	}

	@Override
	public Transaction getDeposit(
			@NotNull @Valid final TransactionIdentifierDetails 
					transactionIdentifierDetails) {
		final Optional<Transaction> optional = transactionRepository
				.findOne(new DepositHasIdAndAccountId(
						transactionIdentifierDetails.getTransactionId(),
						transactionIdentifierDetails.getAccountId()));
		if(optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public List<Transaction> getDeposits(@NotNull @Valid final EntityId accountId) {
		final Optional<Account> optional = accountRepository
				.findById(accountId);
		if(optional.isPresent()) {
			return optional.get().getDeposits();
		}

		return new ArrayList<Transaction>();
	}

	@Override
	public Transaction getTransaction(
			@NotNull @Valid final TransactionIdentifierDetails
					transactionIdentifierDetails) {
		final Optional<Transaction> optional = transactionRepository
				.findOne(new TransactionHasIdAndAccountId(
						transactionIdentifierDetails.getTransactionId(),
						transactionIdentifierDetails.getAccountId()));
		if(optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public List<Transaction> getTransactions(
			@NotNull @Valid final EntityId accountId) {
		final Optional<Account> optional = accountRepository
				.findById(accountId);
		if(optional.isPresent()) {
			return optional.get().getTransactions();
		}

		return null;
	}

	@Override
	public Transaction getTransfer(
			@NotNull @Valid final TransferIdentifierDetails
					transferIdentifierDetails) {
		final Optional<Transaction> optional = transactionRepository
				.findByAccountIdAndJournalCodeAndIsCorrection(
						transferIdentifierDetails.getAccountId(),
						transferIdentifierDetails.getJournalCode(),
						transferIdentifierDetails.isCorrection());
		if(optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public Transaction getWithdrawal(
			@NotNull @Valid final TransactionIdentifierDetails
					transactionIdentifierDetails) {
		final Optional<Transaction> optional = transactionRepository
				.findOne(new WithdrawalHasIdAndAccountId(
						transactionIdentifierDetails.getTransactionId(),
						transactionIdentifierDetails.getAccountId()));
		if(optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public List<Transaction> getWithdrawals(
			@NotNull @Valid final EntityId accountId) {
		final Optional<Account> optional = accountRepository
				.findById(accountId);
		if(optional.isPresent()) {
			return optional.get().getWithdrawals();
		}

		return new ArrayList<Transaction>();
	}

	@Override
	public Transaction withdrawFunds(
			@NotNull @Valid TransactionDetails transactionDetails) {
		final Optional<Account> optional = accountRepository
				.findById(transactionDetails.getAccountId());
		if(optional.isPresent()) {
			final Account account = optional.get();
			final Transaction transaction = account
					.withdrawFunds(transactionDetails.getAmount(),
							transactionDetails.getDescription(),
							transactionDetails.getReferenceAccountId());

			accountRepository.save(account);

			return transaction;
		}
		return null;
	}

	@Override
	public Account openAccount(@NotNull @Valid String ownerId, @NotEmpty String name) {
		final Account.Builder builder = accountBuilderFactory.create();
		final Account account = builder
				.forOwner(ownerId)
				.withName(name)
				.withOpeningBalance(new Balance("0.00"))
				.build();

		return accountRepository.save(account);
	}
}