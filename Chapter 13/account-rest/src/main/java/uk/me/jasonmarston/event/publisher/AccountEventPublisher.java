package uk.me.jasonmarston.event.publisher;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "objectOutputChannel")
public interface AccountEventPublisher {
	<T> void publish(T event);
}
