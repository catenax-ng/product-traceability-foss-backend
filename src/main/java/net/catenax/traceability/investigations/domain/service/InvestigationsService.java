package net.catenax.traceability.investigations.domain.service;

import net.catenax.traceability.assets.domain.model.InvestigationStatus;
import net.catenax.traceability.investigations.domain.model.Investigation;
import net.catenax.traceability.investigations.domain.model.Notification;
import net.catenax.traceability.investigations.domain.ports.EDCUrlProvider;
import net.catenax.traceability.investigations.domain.ports.InvestigationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestigationsService {

	private final InvestigationsRepository repository;

	private final EDCUrlProvider edcUrlProvider;

	public InvestigationsService(InvestigationsRepository repository, EDCUrlProvider edcUrlProvider) {
		this.repository = repository;
		this.edcUrlProvider = edcUrlProvider;
	}

	public void startInvestigation(List<String> assetIds, String description) {
		repository.save(new Investigation(assetIds, description));
	}

	public void updateInvestigationStatus(Long investigationId, InvestigationStatus status) {
		if (status == InvestigationStatus.APPROVED || status == InvestigationStatus.DECLINED) {
			Investigation investigation = repository.getInvestigation(investigationId);

			investigation.updateStatus(status);
			investigation.getNotifications().forEach(this::updateEdcUrl);

			repository.update(investigation);
		}
	}

	private void updateEdcUrl(Notification notification) {
		String edcUrl = edcUrlProvider.getEdcUrl(notification.getBpnNumber());
		notification.setEdcUrl(edcUrl);
	}

}
