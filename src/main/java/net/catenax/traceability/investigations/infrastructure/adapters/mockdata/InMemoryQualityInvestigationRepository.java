package net.catenax.traceability.investigations.infrastructure.adapters.mockdata;

import net.catenax.traceability.investigations.domain.model.Issuer;
import net.catenax.traceability.investigations.domain.model.QualityInvestigation;
import net.catenax.traceability.investigations.domain.ports.QualityInvestigationRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyList;

@Component
public class InMemoryQualityInvestigationRepository implements QualityInvestigationRepository {

	private final ConcurrentHashMap<Issuer, List<QualityInvestigation>> storage;

	public InMemoryQualityInvestigationRepository() {
		this.storage = new ConcurrentHashMap<>();
	}

	@Override
	public List<QualityInvestigation> findAll(Issuer issuer) {
		return storage.getOrDefault(issuer, emptyList());
	}

	@Override
	public void save(QualityInvestigation qualityInvestigation) {
		List<QualityInvestigation> investigations = storage.compute(qualityInvestigation.issuer(), (k, v) -> (v == null) ? new ArrayList<>() : v);
		investigations.add(qualityInvestigation);
	}
}
