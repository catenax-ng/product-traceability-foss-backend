package net.catenax.traceability.investigations.domain.model;

import java.util.UUID;

import static org.springframework.util.Assert.hasText;

public record QualityInvestigationId(String raw) {

	public QualityInvestigationId {
		validate(raw);
	}

	public static QualityInvestigationId random() {
		return new QualityInvestigationId(UUID.randomUUID().toString());
	}

	private void validate(String raw) {
		hasText(raw, "Can't create QualityInvestigationId from blank string");
	}
}
