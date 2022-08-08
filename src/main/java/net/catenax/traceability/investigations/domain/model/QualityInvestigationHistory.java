package net.catenax.traceability.investigations.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.List.of;
import static java.util.stream.Collectors.groupingBy;
import static org.springframework.util.Assert.notNull;

public class QualityInvestigationHistory {

	private final Map<QualityInvestigationType, List<QualityInvestigation>> qualityInvestigationsByType;

	private QualityInvestigationHistory(List<QualityInvestigation> qualityInvestigations) {
		validate(qualityInvestigations);
		this.qualityInvestigationsByType = groupByType(qualityInvestigations);
	}

	private static Map<QualityInvestigationType, List<QualityInvestigation>> groupByType(List<QualityInvestigation> qualityInvestigations) {
		return qualityInvestigations.stream()
			.collect(groupingBy(QualityInvestigation::qualityInvestigationType));
	}

	static QualityInvestigationHistory empty() {
		return new QualityInvestigationHistory(of());
	}

	public static QualityInvestigationHistory from(List<QualityInvestigation> qualityInvestigations) {
		return new QualityInvestigationHistory(qualityInvestigations);
	}

	private void validate(List<QualityInvestigation> qualityInvestigationByType) {
		notNull(qualityInvestigationByType, "Quality Investigation Collection can't be null");
		qualityInvestigationByType.forEach(value -> notNull(value, "QualityInvestigation can't be null"));
	}

	public void add(QualityInvestigation qualityInvestigation) {
		List<QualityInvestigation> qualityInvestigationForType
			= qualityInvestigationsByType.compute(qualityInvestigation.qualityInvestigationType(), (k, v) -> (v == null) ? new ArrayList<>() : v);

		qualityInvestigationForType.add(qualityInvestigation);
	}
}
