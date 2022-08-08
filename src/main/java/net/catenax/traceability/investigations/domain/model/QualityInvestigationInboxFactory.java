package net.catenax.traceability.investigations.domain.model;

import net.catenax.traceability.investigations.domain.ports.QualityInvestigationRepository;

import java.util.List;

import static org.springframework.util.Assert.notNull;

public class QualityInvestigationInboxFactory {

	private final QualityInvestigationRepository qualityInvestigationRepository;

	public QualityInvestigationInboxFactory(QualityInvestigationRepository qualityInvestigationRepository) {
		this.qualityInvestigationRepository = qualityInvestigationRepository;
	}

	public QualityInvestigationInbox create(Issuer issuer) {
		notNull(issuer, "Issuer can't be null");

		List<QualityInvestigation> qualityInvestigations = qualityInvestigationRepository.findAll(issuer);

		return QualityInvestigationInbox.from(issuer, qualityInvestigations);
	}
}
