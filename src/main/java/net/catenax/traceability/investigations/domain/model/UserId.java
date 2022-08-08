package net.catenax.traceability.investigations.domain.model;

import static org.springframework.util.Assert.hasText;

public record UserId(String raw) {

	public UserId {
		hasText(raw, "Can't create UserId from blank string");
	}
}
