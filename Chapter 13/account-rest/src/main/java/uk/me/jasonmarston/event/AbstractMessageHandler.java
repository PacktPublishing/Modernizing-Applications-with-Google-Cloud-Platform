package uk.me.jasonmarston.event.handler;

import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

public abstract class AbstractMessageHandler implements MessageHandler {
	protected final void ack(final Message<?> message) {
		getOriginalMessage(message).ack();
	}

	protected final void nack(final Message<?> message) {
		getOriginalMessage(message).nack();
	}
	
	private BasicAcknowledgeablePubsubMessage getOriginalMessage(
			final Message<?> message) {
		return message.getHeaders().get(
				GcpPubSubHeaders.ORIGINAL_MESSAGE,
				BasicAcknowledgeablePubsubMessage.class);
	}
}
