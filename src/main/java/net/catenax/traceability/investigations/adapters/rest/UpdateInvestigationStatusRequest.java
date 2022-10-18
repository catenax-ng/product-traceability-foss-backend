package net.catenax.traceability.investigations.adapters.rest;

import net.catenax.traceability.assets.domain.model.InvestigationStatus;

import javax.validation.constraints.NotNull;

public record UpdateInvestigationStatusRequest(
	@NotNull InvestigationStatus status
) {}
