package net.catenax.traceability.investigations.domain.model;

import static org.springframework.util.Assert.hasText;

public record PartId(String raw) {

	public PartId {
		validate(raw);
	}

	private void validate(String raw) {
		hasText(raw, "Can't create PartId from blank string");
	}
}
