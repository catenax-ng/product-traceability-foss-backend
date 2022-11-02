package net.catenax.traceability.investigations.domain.model;

import net.catenax.traceability.investigations.domain.model.exception.NotificationStatusTransitionNotAllowed;

import java.util.ArrayList;
import java.util.List;

public class Notification {
	private Long id;
	private String bpnNumber;
	private String edcUrl;
	private String contractAgreementId;
	private List<AffectedPart> affectedParts = new ArrayList<>();
	private String description;
	private InvestigationStatus investigationStatus;

	public Notification(Long id, String bpnNumber, String edcUrl, String contractAgreementId, String description, InvestigationStatus investigationStatus, List<AffectedPart> affectedParts) {
		this.id = id;
		this.bpnNumber = bpnNumber;
		this.edcUrl = edcUrl;
		this.contractAgreementId = contractAgreementId;
		this.description = description;
		this.investigationStatus = investigationStatus;

		if (affectedParts != null) {
			this.affectedParts = affectedParts;
		}
	}

	void changeStatusTo(InvestigationStatus to) {
		boolean transitionAllowed = investigationStatus.transitionAllowed(to);

		if (!transitionAllowed) {
			throw new NotificationStatusTransitionNotAllowed(id, investigationStatus, to);
		}

		this.investigationStatus = to;
	}

	public Long getId() {
		return id;
	}

	public String getContractAgreementId() {
		return contractAgreementId;
	}

	public void setContractAgreementId(String contractAgreementId) {
		this.contractAgreementId = contractAgreementId;
	}

	public List<AffectedPart> getAffectedParts() {
		return affectedParts;
	}

	public String getBpnNumber() {
		return bpnNumber;
	}

	public void setEdcUrl(String edcUrl) {
		this.edcUrl = edcUrl;
	}

	public String getEdcUrl() {
		return edcUrl;
	}

	public String getDescription() {
		return description;
	}

	public InvestigationStatus getInvestigationStatus() {
		return investigationStatus;
	}
}
