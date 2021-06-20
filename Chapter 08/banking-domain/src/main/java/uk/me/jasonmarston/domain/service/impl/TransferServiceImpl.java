package uk.me.jasonmarston.domain.service.impl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.details.impl.TransactionDetails;
import uk.me.jasonmarston.domain.details.impl.TransferDetails;
import uk.me.jasonmarston.domain.service.AccountService;
import uk.me.jasonmarston.domain.service.TransferService;

@Service
@Validated
@Transactional(propagation = Propagation.REQUIRED,
		isolation = Isolation.REPEATABLE_READ,
		readOnly = false)
public class TransferServiceImpl implements TransferService {

	@Autowired
	@Lazy
	private AccountService accountService;

	@Override
	public void transferFunds(
			@NotNull @Valid final TransferDetails transferDetails) {
		final TransactionDetails withdrawal = new TransactionDetails(
				transferDetails.getFromAccountId(),
				transferDetails.getAmount());

		final TransactionDetails deposit = new TransactionDetails(
				transferDetails.getToAccountId(),
				transferDetails.getAmount());

		accountService.withdrawFunds(withdrawal);
		accountService.depositFunds(deposit);
	}
}