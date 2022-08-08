package net.catenax.traceability.investigations.domain.ports;

import net.catenax.traceability.investigations.domain.model.Issuer;
import net.catenax.traceability.investigations.domain.model.QualityInvestigation;

import java.util.List;

public interface QualityInvestigationRepository {
	List<QualityInvestigation> findAll(Issuer issuer);

	void save(QualityInvestigation qualityInvestigation);
}
