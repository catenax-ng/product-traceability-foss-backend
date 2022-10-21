package net.catenax.traceability.investigations.domain.model.exception;

import net.catenax.traceability.investigations.domain.model.InvestigationId;

public class InvestigationNotFoundException extends RuntimeException {

	public InvestigationNotFoundException(InvestigationId investigationId) {
		super("Investigation not found for %s id".formatted(investigationId.value()));
	}
}
