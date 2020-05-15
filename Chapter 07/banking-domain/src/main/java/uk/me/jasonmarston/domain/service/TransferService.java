package uk.me.jasonmarston.domain.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.details.impl.TransferDetails;

public interface TransferService {
	void transferFunds(@NotNull @Valid final TransferDetails transferDetails);
}