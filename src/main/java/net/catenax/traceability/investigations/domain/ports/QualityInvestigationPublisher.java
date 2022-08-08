package net.catenax.traceability.investigations.domain.ports;

import net.catenax.traceability.investigations.domain.model.QualityInvestigation;

public interface QualityInvestigationPublisher {
	void publish(QualityInvestigation qualityInvestigation);
}
