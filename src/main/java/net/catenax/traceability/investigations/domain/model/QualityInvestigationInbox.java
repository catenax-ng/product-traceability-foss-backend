package net.catenax.traceability.investigations.domain.model;

import java.util.List;

import static org.springframework.util.Assert.notNull;

public class QualityInvestigationInbox {

	private final Issuer issuer;

	private final QualityInvestigationHistory qualityInvestigationHistory;

	private QualityInvestigationInbox(Issuer issuer, List<QualityInvestigation> qualityInvestigations) {
		notNull(issuer, "Issuer can't be null");
		notNull(qualityInvestigations, "QualityInvestigation collection can't be null");
		this.issuer = issuer;
		this.qualityInvestigationHistory = QualityInvestigationHistory.from(qualityInvestigations);
	}

	public static QualityInvestigationInbox from(Issuer issuer, List<QualityInvestigation> qualityInvestigations) {
		return new QualityInvestigationInbox(issuer, qualityInvestigations);
	}

	public QualityInvestigationId add(QualityInvestigation qualityInvestigation) {
		notNull(qualityInvestigation, "QualityInvestigation can't be null");

		qualityInvestigationHistory.add(qualityInvestigation);

		return qualityInvestigation.qualityInvestigationId();
	}
}
