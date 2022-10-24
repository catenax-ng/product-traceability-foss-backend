package net.catenax.traceability.investigations.domain.service;

import net.catenax.traceability.assets.infrastructure.config.async.AssetsAsyncConfig;
import net.catenax.traceability.infrastructure.edc.blackbox.InvestigationsEDCFacade;
import net.catenax.traceability.investigations.domain.model.Notification;
import net.catenax.traceability.investigations.domain.ports.InvestigationsRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationsService {

	private final InvestigationsEDCFacade edcFacade;
	private final InvestigationsRepository repository;

	public NotificationsService(InvestigationsEDCFacade edcFacade, InvestigationsRepository repository) {
		this.edcFacade = edcFacade;
		this.repository = repository;
	}

	@Async(value = AssetsAsyncConfig.UPDATE_NOTIFICATION_EXECUTOR)
	public void updateAsync(Notification notification) {
		edcFacade.startEDCTransfer(notification);
		repository.update(notification);
	}
}
