package net.catenax.traceability.investigations.domain.service;

import net.catenax.traceability.investigations.domain.model.Issuer;
import net.catenax.traceability.investigations.domain.model.PartId;
import net.catenax.traceability.investigations.domain.model.QualityInvestigation;
import net.catenax.traceability.investigations.domain.model.QualityInvestigationId;
import net.catenax.traceability.investigations.domain.model.QualityInvestigationInboxFactory;
import net.catenax.traceability.investigations.domain.model.QualityInvestigationType;
import net.catenax.traceability.investigations.domain.model.UserId;
import net.catenax.traceability.investigations.domain.ports.QualityInvestigationPublisher;
import net.catenax.traceability.investigations.domain.ports.QualityInvestigationRepository;

import java.time.InstantSource;
import java.util.stream.Collectors;

public class AddQualityInvestigationService {

	private final QualityInvestigationInboxFactory qualityInvestigationInboxFactory;
	private final QualityInvestigationRepository qualityInvestigationRepository;
	private final QualityInvestigationPublisher qualityInvestigationPublisher;
	private final InstantSource instantSource;

	public AddQualityInvestigationService(QualityInvestigationInboxFactory qualityInvestigationInboxFactory,
										  QualityInvestigationRepository qualityInvestigationRepository,
										  QualityInvestigationPublisher qualityInvestigationPublisher,
										  InstantSource instantSource) {
		this.qualityInvestigationInboxFactory = qualityInvestigationInboxFactory;
		this.qualityInvestigationRepository = qualityInvestigationRepository;
		this.qualityInvestigationPublisher = qualityInvestigationPublisher;
		this.instantSource = instantSource;
	}

	public QualityInvestigationId handle(AddQualityInvestigation request) {
		var issuer = new Issuer(new UserId(request.userId()));

		var qualityInvestigation = toDomain(issuer, request);

		var qualityInvestigationInbox = qualityInvestigationInboxFactory.create(issuer);

		var investigationId = qualityInvestigationInbox.add(qualityInvestigation);

		qualityInvestigationRepository.save(qualityInvestigation);
		qualityInvestigationPublisher.publish(qualityInvestigation);

		return investigationId;
	}

	private QualityInvestigation toDomain(Issuer issuer, AddQualityInvestigation request) {
		return new QualityInvestigation(
			issuer,
			QualityInvestigationId.random(),
			QualityInvestigationType.QUEUED,
			request.partIds().stream().map(PartId::new).collect(Collectors.toSet()),
			request.description(),
			instantSource.instant()
		);
	}
}
