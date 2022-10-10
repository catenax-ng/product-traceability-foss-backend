package net.catenax.traceability.investigations.domain.service;

import net.catenax.traceability.assets.domain.ports.AssetRepository;
import net.catenax.traceability.investigations.domain.model.Investigation;
import net.catenax.traceability.investigations.domain.ports.InvestigationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestigationsService {

	private final InvestigationsRepository repository;

	private final AssetRepository assetRepository;

	public InvestigationsService(InvestigationsRepository repository, AssetRepository assetRepository) {
		this.repository = repository;
		this.assetRepository = assetRepository;
	}

	public void startInvestigation(List<String> assetIds, String description) {
		repository.save(new Investigation(assetIds, description));
	}

}
