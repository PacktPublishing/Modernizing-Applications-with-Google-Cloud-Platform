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
import uk.me.jasonmarston.event.handler.AbstractMessageHandler;
import uk.me.jasonmarston.event.impl.DepositFailedEvent;
import uk.me.jasonmarston.event.impl.WithdrawalSucceededEvent;
import uk.me.jasonmarston.event.publisher.impl.AccountEventPublisher;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class WithdrawalSuccessHandler extends AbstractMessageHandler {
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TransactionDetailsBuilderFactory transactionDetailsBuilderFactory;
    
    @Autowired
    private TransferIdentifierDetailsBuilderFactory transferIdentifierDetailsBuilderFactory;
    
    @Autowired
    private AccountEventPublisher accountEventPublisher;

    @Override
	public void handleMessage(final Message<?> message)
			throws MessagingException {
		final WithdrawalSucceededEvent event = 
				(WithdrawalSucceededEvent) message.getPayload();
		// only process transfers
		if(event.getAccountId().equals(event.getReferenceAccountId())) {
			this.ack(message);
			return;
		}
		
		final TransactionDetails.Builder transactionDetailsBuilder = 
				transactionDetailsBuilderFactory.create();
		
		final TransactionDetails transactionDetails = transactionDetailsBuilder
				.forAccountId(new EntityId(event.getReferenceAccountId()))
				.withReferenceAccountId(new EntityId(event.getAccountId()))
				.withAmount(new Amount(event.getAmount().toString()))
				.withDescription(event.getDescription())
				.withJournalCode(new EntityId(event.getJournalCode()))
				.asCorrection(event.isCorrection())
				.build();
		
		final TransferIdentifierDetails.Builder transactionIdentifierBuilder = 
				transferIdentifierDetailsBuilderFactory.create();

		final TransferIdentifierDetails identifierDetails = 
				transactionIdentifierBuilder
						.forAccountId(transactionDetails.getAccountId())
						.forJournalCode(transactionDetails.getJournalCode())
						.asCorrection(transactionDetails.isCorrection())
						.build();

		try {
			if(accountService.getTransfer(identifierDetails) == null) {
				accountService.depositFunds(transactionDetails);
			}
			this.ack(message);
		}
		catch(final RuntimeException e) {
			if(accountService.getTransfer(identifierDetails) != null) {
				this.ack(message); // another handler instance worked
				return;
			}
			if(!identifierDetails.isCorrection()) {
				final TransactionDetails failureDetails = 
						transactionDetailsBuilder
								.asCorrection(true)
								.build();
				final DepositFailedEvent depositFailedEvent = 
						new DepositFailedEvent(failureDetails);
				accountEventPublisher.publish(depositFailedEvent);
			}
			this.ack(message);
		}
	}
}
