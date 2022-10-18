package net.catenax.traceability.investigations.domain.ports;

import net.catenax.traceability.investigations.domain.model.Investigation;
import net.catenax.traceability.investigations.domain.model.Notification;

public interface InvestigationsRepository {
	void save(Investigation investigation);
	Investigation getInvestigation(Long investigationId);
	void update(Investigation investigation);
	void update(Notification notification);
}
