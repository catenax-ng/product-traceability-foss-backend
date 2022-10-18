package net.catenax.traceability.investigations.domain.model;

import net.catenax.traceability.assets.domain.model.InvestigationStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Investigation {
	private Long id;
	private List<String> assetIds;
	private InvestigationStatus investigationStatus;
	private String description;
	private Map<Long, Notification> notifications;

	public Investigation(List<String> assetIds, String description) {
		this.assetIds = assetIds;
		this.description = description;
		this.investigationStatus = InvestigationStatus.CREATED;
	}

	public Investigation(Long id, InvestigationStatus investigationStatus, String description, List<String> assetIds, List<Notification> notifications) {
		this.id = id;
		this.assetIds = assetIds;
		this.investigationStatus = investigationStatus;
		this.description = description;
		this.notifications = notifications.stream()
			.collect(Collectors.toMap(Notification::getId, Function.identity()));
	}

	public Long getId() {
		return id;
	}

	public List<Notification> getNotifications() {
		return new ArrayList<>(notifications.values());
	}

	public Optional<Notification> getNotification(Long notificationId) {
		return Optional.ofNullable(notifications.get(notificationId));
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

	public void updateStatus(InvestigationStatus investigationStatus) {
		this.investigationStatus = investigationStatus;
	}
}
