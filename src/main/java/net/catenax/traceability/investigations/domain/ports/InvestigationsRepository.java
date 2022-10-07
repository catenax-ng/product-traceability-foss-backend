package net.catenax.traceability.investigations.domain.ports;

import java.util.List;

public interface InvestigationsRepository {
	void startInvestigation(List<String> assetIds, String description);
}
