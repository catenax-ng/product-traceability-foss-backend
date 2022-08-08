package net.catenax.traceability.investigations.infrastructure.adapters.rest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

public record AddQualityInvestigationRequest(
	@NotEmpty(message = "partIds must be present") Set<String> partIds,
	@NotBlank(message = "description must be present") String description) {
}
