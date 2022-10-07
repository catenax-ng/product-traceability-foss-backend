package net.catenax.traceability.investigations.domain.model;

import net.catenax.traceability.assets.domain.model.InvestigationStatus;

public class Investigation {
	private Long investigationId;
	private final String assetId;
	private InvestigationStatus investigationStatus;
	private String description;

	public Investigation(String assetId, String description) {
		this.assetId = assetId;
		this.description = description;
		this.investigationStatus = InvestigationStatus.PENDING;
	}

	public Long getInvestigationId() {
		return investigationId;
	}

	public String getAssetId() {
		return assetId;
	}

	public InvestigationStatus getInvestigationStatus() {
		return investigationStatus;
	}

	public String getDescription() {
		return description;
	}
}
