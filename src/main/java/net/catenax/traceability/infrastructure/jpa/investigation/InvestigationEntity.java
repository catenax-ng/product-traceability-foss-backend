package net.catenax.traceability.infrastructure.jpa.investigation;

import net.catenax.traceability.assets.domain.model.InvestigationStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "investigation")
public class InvestigationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String assetId;
	private InvestigationStatus status;
	private String description;
	private ZonedDateTime created;
	private ZonedDateTime updated;

	public InvestigationEntity() {
	}

	public InvestigationEntity(String assetId, String description, InvestigationStatus status) {
		ZonedDateTime now = ZonedDateTime.now();
		this.assetId = assetId;
		this.status = status;
		this.description = description;
		this.created = now;
		this.updated = now;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public InvestigationStatus getStatus() {
		return status;
	}

	public void setStatus(InvestigationStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ZonedDateTime getCreated() {
		return created;
	}

	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	public ZonedDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(ZonedDateTime updated) {
		this.updated = updated;
	}
}
