package net.catenax.traceability.infrastructure.edc.blackbox;

import net.catenax.traceability.infrastructure.edc.blackbox.model.EDCNotification;
import net.catenax.traceability.investigations.domain.service.InvestigationsReceiverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class EdcController {
	private static final Logger logger = LoggerFactory.getLogger(EdcController.class);

	private final InvestigationsReceiverService investigationsReceiverService;

	public EdcController(InvestigationsReceiverService investigationsReceiverService) {
		this.investigationsReceiverService = investigationsReceiverService;
	}

	/**
	 * Receiver API call for EDC Transfer
	 */
	@GetMapping("/qualitynotifications/receive")
	public void qualityNotificationReceive(@RequestBody EDCNotification edcNotification) {
		logger.info("EdcController [qualityNotificationReceive] notificationId:{}", edcNotification);

		investigationsReceiverService.handle(edcNotification);
	}
}

