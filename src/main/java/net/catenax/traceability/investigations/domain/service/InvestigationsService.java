package net.catenax.traceability.investigations.domain.service;

import net.catenax.traceability.investigations.domain.model.Investigation;
import net.catenax.traceability.investigations.domain.ports.InvestigationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestigationsService {

	private final InvestigationsRepository repository;

	public InvestigationsService(InvestigationsRepository repository) {
		this.repository = repository;
	}

	public void startInvestigation(List<String> assetIds, String description) {
		repository.saveAll(assetIds.stream()
			.map(asset -> new Investigation(asset, description))
			.toList()
		);
	}

}
