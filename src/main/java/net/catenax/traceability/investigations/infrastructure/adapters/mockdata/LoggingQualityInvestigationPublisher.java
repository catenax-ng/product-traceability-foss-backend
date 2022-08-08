package net.catenax.traceability.investigations.infrastructure.adapters.mockdata;

import net.catenax.traceability.investigations.domain.model.QualityInvestigation;
import net.catenax.traceability.investigations.domain.ports.QualityInvestigationPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingQualityInvestigationPublisher implements QualityInvestigationPublisher {

	private static final Logger logger = LoggerFactory.getLogger(LoggingQualityInvestigationPublisher.class);

	@Override
	public void publish(QualityInvestigation qualityInvestigation) {
		logger.info("Publishing {}", qualityInvestigation);
	}
}
