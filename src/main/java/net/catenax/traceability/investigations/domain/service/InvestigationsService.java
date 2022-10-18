package net.catenax.traceability.investigations.domain.service;

import net.catenax.traceability.assets.domain.model.InvestigationStatus;
import net.catenax.traceability.infrastructure.edc.blackbox.InvestigationsEDCFacade;
import net.catenax.traceability.investigations.domain.model.Investigation;
import net.catenax.traceability.investigations.domain.model.Notification;
import net.catenax.traceability.investigations.domain.ports.InvestigationsRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestigationsService {

	private final InvestigationsRepository repository;

	private final InvestigationsEDCFacade edcFacade;

	public InvestigationsService(InvestigationsRepository repository, InvestigationsEDCFacade edcFacade) {
		this.repository = repository;
		this.edcFacade = edcFacade;
	}

	public void startInvestigation(List<String> assetIds, String description) {
		repository.save(new Investigation(assetIds, description));
	}

	public void updateInvestigationStatus(Long investigationId, InvestigationStatus status) {
		if (status == InvestigationStatus.APPROVED || status == InvestigationStatus.DECLINED) {
			Investigation investigation = repository.getInvestigation(investigationId);

			investigation.updateStatus(status);
			repository.update(investigation);

			investigation.getNotifications().forEach(this::transferToEDC);
		}
	}

	@Async
	public void transferToEDC(Notification notification) {
		edcFacade.startEDCTransfer(notification);
		repository.update(notification);
	}

}
