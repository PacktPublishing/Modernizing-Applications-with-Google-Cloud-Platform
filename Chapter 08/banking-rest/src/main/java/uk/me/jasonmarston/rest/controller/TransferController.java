package uk.me.jasonmarston.rest.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import uk.me.jasonmarston.domain.value.Amount;

public interface TransferController {
	ResponseEntity<?> transferFunds(final UUID fromId,
			final UUID toId,
			final Amount amount);
}
