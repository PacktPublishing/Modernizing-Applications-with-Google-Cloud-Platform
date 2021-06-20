package uk.me.jasonmarston.rest.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.rest.bean.impl.AccountNameBean;
import uk.me.jasonmarston.rest.bean.impl.DepositBean;
import uk.me.jasonmarston.rest.bean.impl.WithdrawalBean;

public interface AccountController {
	ResponseEntity<?> depositFunds(final UUID id, final DepositBean deposit, final User user);
	ResponseEntity<?> getAccount(final UUID id, final User user);
	ResponseEntity<?> getAccounts(final User user);
	ResponseEntity<?> getBalance(final UUID id, final User user);
	ResponseEntity<?> getDeposit(final UUID accountId, final UUID id, final User user);
	ResponseEntity<?> getDeposits(final UUID id, final User user);
	ResponseEntity<?> getTransaction(final UUID accountId, final UUID id, final User user);
	ResponseEntity<?> getTransactions(final UUID id, final User user);
	ResponseEntity<?> getWithdrawal(final UUID accountId, final UUID id, final User user);
	ResponseEntity<?> getWithdrawals(final UUID id, final User user);
	ResponseEntity<?> openAccount(final AccountNameBean accountNameBean, final User user);
	ResponseEntity<?> withdrawFunds(final UUID id, final WithdrawalBean withdrawal, final User user);
}
