package net.catenax.traceability.investigations.domain.model;

import java.time.Instant;
import java.util.Set;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.state;

public record QualityInvestigation(
	Issuer issuer,
	QualityInvestigationId qualityInvestigationId,
	QualityInvestigationType qualityInvestigationType,
	Set<PartId> partIds,
	String description,
	Instant createdAt
) {

	private static final int DESCRIPTION_MAXIMUM_CHARACTERS = 1000;

	public QualityInvestigation {
		validate(issuer, qualityInvestigationId, qualityInvestigationType, partIds, description, createdAt);
	}

	private void validate(Issuer issuer,
						  QualityInvestigationId qualityInvestigationId,
						  QualityInvestigationType qualityInvestigationType,
						  Set<PartId> partIds,
						  String description,
						  Instant createdAt
	) {
		notNull(issuer, "Issuer can't be null");
		notNull(qualityInvestigationId, "QualityInvestigationId can't be null");
		notNull(qualityInvestigationType, "QualityInvestigationType can't be null");
		notNull(partIds, "PartId collection can't be null");
		partIds.forEach(it -> notNull(it, "PartId can't be null"));
		hasText(description, "Description must has text");
		state(description.length() < DESCRIPTION_MAXIMUM_CHARACTERS,
			"Description can't exceed %s maximum characters".formatted(DESCRIPTION_MAXIMUM_CHARACTERS)
		);
		notNull(createdAt, "Creation date can't be null");
	}
}
