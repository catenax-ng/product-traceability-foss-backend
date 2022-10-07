package net.catenax.traceability.investigations.adapters.jpa;

import net.catenax.traceability.infrastructure.jpa.investigation.InvestigationEntity;
import net.catenax.traceability.infrastructure.jpa.investigation.JpaInvestigationRepository;
import net.catenax.traceability.investigations.domain.model.Investigation;
import net.catenax.traceability.investigations.domain.ports.InvestigationsRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersistentInvestigationsRepository implements InvestigationsRepository {

	private final JpaInvestigationRepository investigationRepository;

	public PersistentInvestigationsRepository(JpaInvestigationRepository investigationRepository) {
		this.investigationRepository = investigationRepository;
	}

	@Override
	public void saveAll(List<Investigation> investigations) {
		investigationRepository.saveAll(investigations.stream()
			.map(this::toNewEntity)
			.toList()
		);
	}

	private InvestigationEntity toNewEntity(Investigation investigation) {
		return new InvestigationEntity(
			investigation.getAssetId(),
			investigation.getDescription(),
			investigation.getInvestigationStatus()
		);
	}

}
