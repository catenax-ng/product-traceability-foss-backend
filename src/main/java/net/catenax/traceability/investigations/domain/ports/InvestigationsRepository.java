package net.catenax.traceability.investigations.domain.ports;

import net.catenax.traceability.investigations.domain.model.Investigation;

public interface InvestigationsRepository {
	void save(Investigation investigation);
}
