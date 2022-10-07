package net.catenax.traceability.investigations.domain.ports;

import net.catenax.traceability.investigations.domain.model.Investigation;

import java.util.List;

public interface InvestigationsRepository {
	void saveAll(List<Investigation> investigations);
}
