package net.catenax.traceability.infrastructure.edc.blackbox.model;

import net.catenax.traceability.assets.domain.model.InvestigationStatus;
import net.catenax.traceability.infrastructure.edc.blackbox.Constants;

import javax.validation.constraints.NotNull;

public record UpdateQualityInvestigationStatus(
	@NotNull(message = Constants.STATUS_MUST_BE_PRESENT) InvestigationStatus status) {
}
