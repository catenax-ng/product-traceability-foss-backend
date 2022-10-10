package net.catenax.traceability.investigations.domain.model;

import net.catenax.traceability.assets.domain.model.InvestigationStatus;

import java.util.List;

public class Investigation {
	private List<String> assetIds;
	private InvestigationStatus investigationStatus;
	private String description;

	public Investigation(List<String> assetIds, String description) {
		this.assetIds = assetIds;
		this.description = description;
		this.investigationStatus = InvestigationStatus.CREATED;
	}

	public List<String> getAssetIds() {
		return assetIds;
	}

	public InvestigationStatus getInvestigationStatus() {
		return investigationStatus;
	}

	public String getDescription() {
		return description;
	}
}
