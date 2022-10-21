package net.catenax.traceability.investigations.domain.model;

import java.util.Objects;

public record InvestigationId(Long value) {

	public InvestigationId(Long value) {
		if (Objects.isNull(value)) {
			throw new IllegalArgumentException("Investigation id must be present");
		}
		this.value = value;
	}
}
