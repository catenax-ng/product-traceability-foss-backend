package net.catenax.traceability.infrastructure.jpa.investigation;

import net.catenax.traceability.investigations.domain.model.InvestigationStatus;
import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.AssetEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "investigation")
public class InvestigationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "assets_investigations",
		joinColumns = @JoinColumn(name = "investigation_id"),
		inverseJoinColumns = @JoinColumn(name = "asset_id")
	)
	private List<AssetEntity> assets;
	private InvestigationStatus status;
	private String description;
	private ZonedDateTime created;
	private ZonedDateTime updated;

	public InvestigationEntity() {
	}

	public InvestigationEntity(List<AssetEntity> assets, String description, InvestigationStatus status) {
		this(assets, status, description, ZonedDateTime.now());
	}

	public InvestigationEntity(List<AssetEntity> assets, InvestigationStatus status, String description, ZonedDateTime created) {
		this.assets = assets;
		this.status = status;
		this.description = description;
		this.created = created;
		this.updated = created;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<AssetEntity> getAssets() {
		return assets;
	}

	public void setAssets(List<AssetEntity> assets) {
		this.assets = assets;
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