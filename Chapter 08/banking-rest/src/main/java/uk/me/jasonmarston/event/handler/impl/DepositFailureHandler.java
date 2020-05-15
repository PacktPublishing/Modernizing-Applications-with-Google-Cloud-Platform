package uk.me.jasonmarston.event.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import uk.me.jasonmarston.domain.details.TransactionDetails;
import uk.me.jasonmarston.domain.details.TransferIdentifierDetails;
import uk.me.jasonmarston.domain.factory.details.TransactionDetailsBuilderFactory;
import uk.me.jasonmarston.domain.factory.details.TransferIdentifierDetailsBuilderFactory;
import uk.me.jasonmarston.domain.service.AccountService;
import uk.me.jasonmarston.domain.value.Amount;
import uk.me.jasonmarston.event.bean.impl.DepositFailedEvent;
import uk.me.jasonmarston.event.handler.AbstractMessageHandler;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class DepositFailureHandler extends AbstractMessageHandler {
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TransactionDetailsBuilderFactory transactionDetailsBuilderFactory;
    
    @Autowired
    private TransferIdentifierDetailsBuilderFactory transferIdentifierDetailsBuilderFactory;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		final DepositFailedEvent event = (DepositFailedEvent) message
				.getPayload();
		// only process transfers
		if(event.getAccountId().equals(event.getReferenceAccountId())) {
			this.ack(message);
			return;
		}

		final TransactionDetails.Builder transactionDetailsBuilder = 
				transactionDetailsBuilderFactory.create();

		final TransactionDetails transactionDetails = transactionDetailsBuilder
				.forAccountId(new EntityId(event.getReferenceAccountId()))
				.withAmount(new Amount(event.getAmount().toString()))
				.withDescription(event.getDescription())
				.withReferenceAccountId(new EntityId(event.getAccountId()))
				.withJournalCode(new EntityId(event.getJournalCode()))
				.asCorrection(event.isCorrection())
				.build();

		final TransferIdentifierDetails.Builder transactionIdentifierBuilder = 
				transferIdentifierDetailsBuilderFactory.create();

		final TransferIdentifierDetails transactionIdentifierDetails = 
				transactionIdentifierBuilder
						.forAccountId(transactionDetails.getAccountId())
						.forJournalCode(transactionDetails.getJournalCode())
						.asCorrection(transactionDetails.isCorrection())
						.build();

		try {
			if(accountService
					.getTransfer(transactionIdentifierDetails) == null) {
				accountService.depositFunds(transactionDetails);
			}
			this.ack(message);
		}
		catch(final RuntimeException e) {
			if(accountService
					.getTransfer(transactionIdentifierDetails) != null) {
				this.ack(message); // another handler instance worked
			}
		}
	}
}
