package uk.me.jasonmarston;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import uk.me.jasonmarston.event.handler.impl.DepositFailureHandler;
import uk.me.jasonmarston.event.handler.impl.WithdrawalSuccessHandler;
import uk.me.jasonmarston.event.impl.DepositFailedEvent;
import uk.me.jasonmarston.event.impl.WithdrawalSucceededEvent;

@Configuration
@EnableIntegration
public class IntegrationConfig {
	@Bean
	@Transformer(
			inputChannel="objectOutputChannel",
			outputChannel="textOutputChannel")
	ObjectToJsonTransformer objectToJsonToTransformer() {
		return new ObjectToJsonTransformer();
	}

	@Bean
	@ServiceActivator(inputChannel = "textOutputChannel")
	public MessageHandler messageSender(final PubSubTemplate pubsubTemplate) {
		return new PubSubMessageHandler(pubsubTemplate, "accountTopic");
	}

	@Bean
	public PubSubInboundChannelAdapter accountMessageChannelAdapter(
			@Qualifier("textInputChannel") final MessageChannel inputChannel,
			final PubSubTemplate pubSubTemplate) {
		PubSubInboundChannelAdapter adapter =
				new PubSubInboundChannelAdapter(
						pubSubTemplate, 
						"accountSubscription");
		adapter.setOutputChannel(inputChannel);
		adapter.setAckMode(AckMode.MANUAL);
		return adapter;
	}

	@Bean
	@Router(inputChannel="textInputChannel")
	public HeaderValueRouter headerValueRouter() {
		final HeaderValueRouter router = 
				new HeaderValueRouter("json__TypeId__");
		router.setChannelMapping(
				"class " + WithdrawalSucceededEvent.class.getCanonicalName(),
				"textWithdrawalSucceededInputChannel");
		router.setChannelMapping(
				"class " + DepositFailedEvent.class.getCanonicalName(),
				"textDepositFailedInputChannel");
		return router;
	}

	@Bean
	public MessageChannel textInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel objectWithdrawalSucceededInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel objectDepositFailedInputChannel() {
		return new DirectChannel();
	}

	@Bean
	@Transformer(
			inputChannel="textWithdrawalSucceededInputChannel",
			outputChannel="objectWithdrawalSucceededInputChannel")
	JsonToObjectTransformer withdrawalSucceededTransformer() {
		return new JsonToObjectTransformer(WithdrawalSucceededEvent.class);
	}

	@Bean
	@Transformer(
			inputChannel="textDepositFailedInputChannel",
			outputChannel="objectDepositFailedInputChannel")
	JsonToObjectTransformer depositFailedTransformer() {
		return new JsonToObjectTransformer(DepositFailedEvent.class);
	}

	@Bean
	@ServiceActivator(inputChannel = "objectWithdrawalSucceededInputChannel")
	public MessageHandler withdrawalSuccededReceiver() {
		return new WithdrawalSuccessHandler();
	}
	
	@Bean
	@ServiceActivator(inputChannel = "objectDepositFailedInputChannel")
	public MessageHandler depositFailedReceiver() {
		return new DepositFailureHandler();
	}
}
