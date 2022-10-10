package net.catenax.traceability.infrastructure.jpa.notification;

import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.AssetEntity;
import net.catenax.traceability.infrastructure.jpa.investigation.InvestigationEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "notification")
public class NotificationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "investigation_id")
	private InvestigationEntity investigation;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "assets_notifications",
		joinColumns = @JoinColumn(name = "notification_id"),
		inverseJoinColumns = @JoinColumn(name = "asset_id")
	)
	private List<AssetEntity> assets;

	private String bpnNumber;
	private String edcUrl;
	private String contractAgreementId;
	private String notificationReferenceId;

	public NotificationEntity() {
	}

	public NotificationEntity(InvestigationEntity investigation, String bpnNumber, List<AssetEntity> assets) {
		this.investigation = investigation;
		this.bpnNumber = bpnNumber;
		this.assets = assets;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public InvestigationEntity getInvestigation() {
		return investigation;
	}

	public void setInvestigation(InvestigationEntity investigationsId) {
		this.investigation = investigationsId;
	}

	public String getBpnNumber() {
		return bpnNumber;
	}

	public void setBpnNumber(String bpnNumber) {
		this.bpnNumber = bpnNumber;
	}

	public String getEdcUrl() {
		return edcUrl;
	}

	public void setEdcUrl(String edcUrl) {
		this.edcUrl = edcUrl;
	}

	public String getContractAgreementId() {
		return contractAgreementId;
	}

	public void setContractAgreementId(String contractAgreementId) {
		this.contractAgreementId = contractAgreementId;
	}

	public String getNotificationReferenceId() {
		return notificationReferenceId;
	}

	public void setNotificationReferenceId(String notificationReferenceId) {
		this.notificationReferenceId = notificationReferenceId;
	}

	public List<AssetEntity> getAssets() {
		return assets;
	}

	public void setAssets(List<AssetEntity> assets) {
		this.assets = assets;
	}
}
