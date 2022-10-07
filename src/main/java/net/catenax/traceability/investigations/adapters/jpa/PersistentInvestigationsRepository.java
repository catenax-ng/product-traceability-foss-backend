package net.catenax.traceability.investigations.adapters.jpa;

import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.JpaAssetsRepository;
import net.catenax.traceability.infrastructure.jpa.investigation.InvestigationEntity;
import net.catenax.traceability.infrastructure.jpa.investigation.JpaInvestigationRepository;
import net.catenax.traceability.investigations.domain.ports.InvestigationsRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersistentInvestigationsRepository implements InvestigationsRepository {

	private final JpaInvestigationRepository investigationRepository;

	private final JpaAssetsRepository assetsRepository;

	public PersistentInvestigationsRepository(JpaInvestigationRepository investigationRepository, JpaAssetsRepository assetsRepository) {
		this.investigationRepository = investigationRepository;
		this.assetsRepository = assetsRepository;
	}

	@Override
	public void startInvestigation(List<String> assetIds, String description) {
		assetsRepository.findByIdIn(assetIds).forEach(asset -> {
			investigationRepository.save(new InvestigationEntity(asset, description));
		});
	}
}
