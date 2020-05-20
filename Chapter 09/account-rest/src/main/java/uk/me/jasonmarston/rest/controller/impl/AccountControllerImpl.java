package uk.me.jasonmarston.rest.controller.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import uk.me.jasonmarston.domain.account.aggregate.impl.Account;
import uk.me.jasonmarston.domain.account.details.impl.TransactionDetails;
import uk.me.jasonmarston.domain.account.details.impl.TransactionIdentifierDetails;
import uk.me.jasonmarston.domain.account.entity.impl.Transaction;
import uk.me.jasonmarston.domain.account.factory.details.TransactionDetailsBuilderFactory;
import uk.me.jasonmarston.domain.account.factory.details.TransactionIdentifierDetailsBuilderFactory;
import uk.me.jasonmarston.domain.account.service.AccountService;
import uk.me.jasonmarston.domain.account.value.impl.Amount;
import uk.me.jasonmarston.domain.user.aggregate.impl.User;
import uk.me.jasonmarston.event.bean.impl.WithdrawalSucceededEvent;
import uk.me.jasonmarston.event.publisher.AccountEventPublisher;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;
import uk.me.jasonmarston.rest.bean.impl.AccountBean;
import uk.me.jasonmarston.rest.bean.impl.AccountNameBean;
import uk.me.jasonmarston.rest.bean.impl.DepositBean;
import uk.me.jasonmarston.rest.bean.impl.MessageBean;
import uk.me.jasonmarston.rest.bean.impl.TransactionBean;
import uk.me.jasonmarston.rest.bean.impl.WithdrawalBean;
import uk.me.jasonmarston.rest.controller.AccountController;

@RestController
public class AccountControllerImpl implements AccountController {
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TransactionDetailsBuilderFactory transactionDetailsBuilderFactory;
    
    @Autowired
    private TransactionIdentifierDetailsBuilderFactory transactionIdentifierDetailsBuilderFactory;
    
    @Autowired
    private AccountEventPublisher accountEventPublisher;

    @Override
    @PreAuthorize("hasRole('USER')")
	@RequestMapping(path = "/account/{id}/deposit",
		method=RequestMethod.POST,
		consumes = "application/json",
		produces = "application/json")
    public ResponseEntity<?> depositFunds(
			@PathVariable("id") final UUID id,
			@NotNull @Valid @RequestBody final DepositBean deposit,
			@AuthenticationPrincipal final User user) {
		final Account account = accountService
				.getAccount(new EntityId(id));
		if(account == null || !account.getOwnerId().equals(user.getUid())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageBean("Invalid Account"));
		}
		if(deposit.getAmount() == null || deposit.getDescription() == null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageBean("Invalid Transaction"));
		}

		final TransactionDetails.Builder builder = 
				transactionDetailsBuilderFactory
					.create();

		final TransactionDetails details = builder
				.forAccountId(new EntityId(id))
				.withAmount(new Amount(deposit.getAmount().toString()))
				.withDescription(deposit.getDescription())
				.build();

		final Transaction transaction = accountService
				.depositFunds(details);

		final URI location = ServletUriComponentsBuilder
	    		.fromCurrentRequest()
	    		.path("/{Id}")
	    		.buildAndExpand(transaction.getId().getId())
	    		.toUri();

		return ResponseEntity.created(location).build();
	}

    @Override
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/account/{id}", 
    	method=RequestMethod.GET,
    	produces = "application/json")
    public ResponseEntity<?> getAccount(@PathVariable("id") final UUID id,
    		@AuthenticationPrincipal final User user) {
   		final Account account = accountService
   				.getAccount(new EntityId(id));
   		if(account == null || !account.getOwnerId().equals(user.getUid())) {
   			return ResponseEntity
   					.badRequest()
   					.body(new MessageBean("Invalid Account"));
   		}

   		return ResponseEntity.ok(new AccountBean(account));
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/account", 
    	method=RequestMethod.GET,
    	produces = "application/json")
    public ResponseEntity<?> getAccounts(
    		@AuthenticationPrincipal final User user) {
   		final List<Account> accounts = accountService
   				.getAccounts(user.getUid());
   		final List<AccountBean> accountBeans = new ArrayList<AccountBean>();
   		for(final Account account : accounts) {
   			accountBeans.add(new AccountBean(account));
   		}

   		return ResponseEntity.ok(accountBeans);
    }

	@Override
    @PreAuthorize("hasRole('USER')")
	@RequestMapping(path = "/account/{id}/balance",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getBalance(
			@PathVariable("id") final UUID id,
			@AuthenticationPrincipal final User user) {
   		final Account account = accountService
   				.getAccount(new EntityId(id));
   		if(account == null || !account.getOwnerId().equals(user.getUid())) {
   			return ResponseEntity
   					.badRequest()
   					.body(new MessageBean("Invalid Account"));
   		}

   		return ResponseEntity
   				.ok(new Amount(account.getBalance().getBalance().toString()));
	}

	@Override
    @PreAuthorize("hasRole('USER')")
	@RequestMapping(path = "/account/{accountId}/deposit/{id}",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getDeposit(
			@PathVariable("accountId") final UUID accountId,
			@PathVariable("id") final UUID id,
			@AuthenticationPrincipal final User user) {
   		final Account account = accountService
   				.getAccount(new EntityId(accountId));
   		if(account == null || !account.getOwnerId().equals(user.getUid())) {
   			return ResponseEntity
   					.badRequest()
   					.body(new MessageBean("Invalid Account"));
   		}

   		final TransactionIdentifierDetails.Builder builder = 
				transactionIdentifierDetailsBuilderFactory
					.create();

		final TransactionIdentifierDetails details = builder
				.forAccountId(new EntityId(accountId))
				.withTransactionId(new EntityId(id))
				.build();
		
		final Transaction deposit = accountService.getDeposit(details);
		if(deposit == null) {
   			return ResponseEntity
   					.badRequest()
   					.body(new MessageBean("Invalid Deposit"));
		}

   		return ResponseEntity.ok(new TransactionBean(deposit));
	}

	@Override
    @PreAuthorize("hasRole('USER')")
	@RequestMapping(path = "/account/{id}/deposit",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getDeposits(
			@PathVariable("id") final UUID id,
			@AuthenticationPrincipal final User user) {
		final Account account = accountService.getAccount(new EntityId(id));
		if(account == null || !account.getOwnerId().equals(user.getUid())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageBean("Invalid Account"));
		}

		final List<Transaction> deposits = account.getDeposits();
		final List<TransactionBean> depositBeans =
				new ArrayList<TransactionBean>();
		for(Transaction deposit : deposits) {
			depositBeans.add(new TransactionBean(deposit));
		}

		return ResponseEntity.ok(depositBeans);
	}

	@Override
    @PreAuthorize("hasRole('USER')")
	@RequestMapping(path = "/account/{accountId}/transaction/{id}",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getTransaction(
			@PathVariable("accountId") final UUID accountId,
			@PathVariable("id") final UUID id,
			@AuthenticationPrincipal final User user) {
		final Account account = accountService.getAccount(new EntityId(id));
		if(account == null || !account.getOwnerId().equals(user.getUid())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageBean("Invalid Account"));
		}

		final TransactionIdentifierDetails.Builder builder = 
				transactionIdentifierDetailsBuilderFactory
					.create();

		final TransactionIdentifierDetails details = builder
				.forAccountId(new EntityId(accountId))
				.build();
		
		final Transaction transaction = accountService.getTransaction(details);
		if(transaction == null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageBean("Invalid Transaction"));
		}

		return ResponseEntity.ok(new TransactionBean(transaction));
	}

	@Override
    @PreAuthorize("hasRole('USER')")
	@RequestMapping(path = "/account/{id}/transaction",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getTransactions(
			@PathVariable("id") final UUID id,
			@AuthenticationPrincipal final User user) {
		final Account account = accountService.getAccount(new EntityId(id));
		if(account == null || !account.getOwnerId().equals(user.getUid())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageBean("Invalid Account"));
		}

		final List<Transaction> transactions = account.getTransactions();
		final List<TransactionBean> transactionBeans =
				new ArrayList<TransactionBean>();
		for(Transaction transaction : transactions) {
			transactionBeans.add(new TransactionBean(transaction));
		}

		return ResponseEntity.ok(transactionBeans);
	}

	@Override
    @PreAuthorize("hasRole('USER')")
	@RequestMapping(path = "/account/{accountId}/withdrawal/{id}",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getWithdrawal(
			@PathVariable("accountId") final UUID accountId,
			@PathVariable("id") final UUID id,
			@AuthenticationPrincipal final User user) {
	   		final Account account = accountService
	   				.getAccount(new EntityId(accountId));
	   		if(account == null 
	   				|| !account.getOwnerId().equals(user.getUid())) {
	   			return ResponseEntity
	   					.badRequest()
	   					.body(new MessageBean("Invalid Account"));
	   		}

	   		final TransactionIdentifierDetails.Builder builder = 
					transactionIdentifierDetailsBuilderFactory
						.create();

			final TransactionIdentifierDetails details = builder
					.forAccountId(new EntityId(accountId))
					.withTransactionId(new EntityId(id))
					.build();
			
			final Transaction withdrawal = accountService
					.getWithdrawal(details);
			if(withdrawal == null) {
	   			return ResponseEntity
	   					.badRequest()
	   					.body(new MessageBean("Invalid Withdrawal"));
			}

	   		return ResponseEntity.ok(new TransactionBean(withdrawal));
	}

	@Override
    @PreAuthorize("hasRole('USER')")
	@RequestMapping(path = "/account/{id}/withdrawal",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> getWithdrawals(
			@PathVariable("id") final UUID id,
			@AuthenticationPrincipal final User user) {
		final Account account = accountService.getAccount(new EntityId(id));
		if(account == null || !account.getOwnerId().equals(user.getUid())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageBean("Invalid Account"));
		}

		final List<Transaction> withdrawals = account.getWithdrawals();
		final List<TransactionBean> withdrawalBeans =
				new ArrayList<TransactionBean>();
		for(Transaction withdrawal : withdrawals) {
			withdrawalBeans.add(new TransactionBean(withdrawal));
		}

		return ResponseEntity.ok(withdrawalBeans);
	}

	@Override
	@RequestMapping(path = "/healthcheck",
		method=RequestMethod.GET,
		produces = "application/json")
	public ResponseEntity<?> healthCheck() {
   		return ResponseEntity.ok(new MessageBean("OK"));
	}

	@Override
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/account", 
    	method=RequestMethod.POST,
    	produces = "application/json")
    public ResponseEntity<?> openAccount(
    		@NotNull @Valid @RequestBody final AccountNameBean accountNameBean,
    		@AuthenticationPrincipal final User user) {
    	final Account account = accountService
    			.openAccount(user.getUid(), accountNameBean.getName());
 
    	final URI location = ServletUriComponentsBuilder
    			.fromCurrentRequest()
    			.path("/{id}")
    			.buildAndExpand(account.getId().getId())
    			.toUri();
 
        return ResponseEntity.created(location).build();
    }

	@Override
    @PreAuthorize("hasRole('USER')")
	@RequestMapping(path = "/account/{id}/withdrawal",
		method=RequestMethod.POST,
		consumes = "application/json",
		produces = "application/json")
	public ResponseEntity<?> withdrawFunds(@PathVariable("id") final UUID id,
			@NotNull @Valid @RequestBody final WithdrawalBean withdrawal,
			@AuthenticationPrincipal final User user) {
		final Account account = accountService
				.getAccount(new EntityId(id));
		if(account == null || !account.getOwnerId().equals(user.getUid())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageBean("Invalid Account"));
		}

		if(withdrawal.getOtherAccount() != null) {
			final Account otherAccount = accountService
					.getAccount(new EntityId(withdrawal.getOtherAccount()));
			if(!otherAccount.getOwnerId().equals(user.getUid())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageBean("Invalid Other Account"));
			}
		}

		final TransactionDetails.Builder builder = 
				transactionDetailsBuilderFactory
					.create();

		builder.forAccountId(new EntityId(id))
				.withAmount(new Amount(withdrawal.getAmount().toString()))
				.withDescription(withdrawal.getDescription());

		if(withdrawal.getOtherAccount() != null
				&& !account.getId().getId().equals(
						withdrawal.getOtherAccount())) {
			builder.withReferenceAccountId(
					new EntityId(withdrawal.getOtherAccount()));
		}

		final TransactionDetails details = builder
				.build();

		final Transaction transaction = accountService
				.withdrawFunds(details);
		
		final TransactionDetails successDetails = builder
				.withJournalCode(transaction.getJournalCode())
				.withReferenceAccountId(transaction.getReferenceAccountId())
				.asCorrection(transaction.isCorrection())
				.build();

		final WithdrawalSucceededEvent event = 
				new WithdrawalSucceededEvent(successDetails);

		accountEventPublisher.publish(event);

		final URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{Id}")
				.buildAndExpand(transaction.getId().getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
}