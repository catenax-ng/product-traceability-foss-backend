package net.catenax.traceability.investigations.domain.model;

import net.catenax.traceability.assets.domain.model.QualityType;

public record AffectedPart(
	String assetId,
	QualityType qualityType)
{}
