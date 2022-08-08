package net.catenax.traceability.investigations.domain.model;

import static org.springframework.util.Assert.notNull;

public record Issuer(UserId userId) {

	public Issuer {
		notNull(userId, "UserId can't be null");
	}
}
