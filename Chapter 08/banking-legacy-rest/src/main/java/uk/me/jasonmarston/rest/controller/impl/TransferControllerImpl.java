package uk.me.jasonmarston.rest.controller.impl;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.me.jasonmarston.domain.value.Amount;
import uk.me.jasonmarston.rest.controller.TransferController;
import uk.me.jasonmarston.rest.controller.message.impl.Message;

@RestController
public class TransferControllerImpl implements TransferController {
//    @Autowired
//    private TransferService transferService;

	@Override
	@RequestMapping(path = "/accounts/{fromId}/transfer/account{toId}",
		method=RequestMethod.POST, 
		consumes = "application/json", 
		produces = "application/json")
	public ResponseEntity<?> transferFunds(
			@PathVariable("fromId") final UUID fromId,
			@PathVariable("toId") final UUID toId,
			@RequestBody Amount amount) {
		try {
//			transferService.transferFunds(new EntityId(fromId), 
//				new EntityId(toId),
//				amount);
			return ResponseEntity
				.ok()
				.body(new Message("Funds Successfully Transferred"));
		}
		catch(NoSuchElementException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message("Invalid Account"));
		}
		catch(RuntimeException e) {
			return ResponseEntity
				.badRequest()
				.body(new Message(e.getMessage()));
		}
	}
}
