package net.catenax.traceability.investigations.domain.service;

import java.util.Set;

public record AddQualityInvestigation(
	String userId,
	Set<String> partIds,
	String description) {
}
