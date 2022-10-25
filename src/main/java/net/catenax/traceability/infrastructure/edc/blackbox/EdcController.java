package net.catenax.traceability.infrastructure.edc.blackbox;

import net.catenax.traceability.infrastructure.edc.blackbox.model.EDCNotification;
import net.catenax.traceability.investigations.domain.model.Notification;
import net.catenax.traceability.investigations.domain.service.InvestigationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class EdcController {
	private static final Logger logger = LoggerFactory.getLogger(EdcController.class);

	private final InvestigationsService investigationsService;

	public EdcController(InvestigationsService investigationsService) {
		this.investigationsService = investigationsService;
	}

	/**
	 * Receiver API call for EDC Transfer
	 */
	@PostMapping("/qualitynotifications/receive")
	public void qualityNotificationReceive(@RequestBody EDCNotification dto) {
		logger.info("EdcController [qualityNotificationReceive] notificationId:{}", dto.getNotificationId());
		investigationsService.receiveNotification(new Notification(
			dto.getNotificationId(),
			dto.getSenderBPN(),
			dto.getSenderAddress(),
			null,
			dto.getInformation(),
			dto.getStatus(),
			dto.getListOfAffectedItems()
		));
	}
}

