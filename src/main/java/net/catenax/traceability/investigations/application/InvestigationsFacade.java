package net.catenax.traceability.investigations.application;

import net.catenax.traceability.investigations.domain.model.QualityInvestigationId;
import net.catenax.traceability.investigations.domain.model.QualityInvestigationInboxFactory;
import net.catenax.traceability.investigations.domain.ports.QualityInvestigationPublisher;
import net.catenax.traceability.investigations.domain.ports.QualityInvestigationRepository;
import net.catenax.traceability.investigations.domain.service.AddQualityInvestigation;
import net.catenax.traceability.investigations.domain.service.AddQualityInvestigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.InstantSource;

@Component
public class InvestigationsFacade {

	private final AddQualityInvestigationService addQualityInvestigationService;

	@Autowired
	public InvestigationsFacade(QualityInvestigationRepository qualityInvestigationRepository,
								QualityInvestigationPublisher qualityInvestigationPublisher,
								InstantSource instantSource) {
		var factory = new QualityInvestigationInboxFactory(qualityInvestigationRepository);

		this.addQualityInvestigationService = new AddQualityInvestigationService(factory,
			qualityInvestigationRepository,
			qualityInvestigationPublisher,
			instantSource);
	}

	public QualityInvestigationId handle(AddQualityInvestigation addQualityInvestigation) {
		return addQualityInvestigationService.handle(addQualityInvestigation);
	}
}
