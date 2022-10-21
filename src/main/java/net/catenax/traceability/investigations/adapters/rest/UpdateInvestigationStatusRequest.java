package net.catenax.traceability.investigations.adapters.rest;

import net.catenax.traceability.investigations.domain.model.InvestigationStatus;

import javax.validation.constraints.NotNull;

public record UpdateInvestigationStatusRequest(
	@NotNull InvestigationStatus status
) {}
