package net.catenax.traceability.investigations.adapters.jpa;

import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.AssetEntity;
import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.JpaAssetsRepository;
import net.catenax.traceability.infrastructure.jpa.investigation.InvestigationEntity;
import net.catenax.traceability.infrastructure.jpa.investigation.JpaInvestigationRepository;
import net.catenax.traceability.infrastructure.jpa.notification.JpaNotificationRepository;
import net.catenax.traceability.infrastructure.jpa.notification.NotificationEntity;
import net.catenax.traceability.investigations.domain.model.Investigation;
import net.catenax.traceability.investigations.domain.model.Notification;
import net.catenax.traceability.investigations.domain.ports.InvestigationsRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PersistentInvestigationsRepository implements InvestigationsRepository {

	private final JpaInvestigationRepository investigationRepository;

	private final JpaAssetsRepository assetsRepository;

	private final JpaNotificationRepository notificationRepository;

	public PersistentInvestigationsRepository(JpaInvestigationRepository investigationRepository, JpaAssetsRepository assetsRepository, JpaNotificationRepository notificationRepository) {
		this.investigationRepository = investigationRepository;
		this.assetsRepository = assetsRepository;
		this.notificationRepository = notificationRepository;
	}


	@Override
	public void save(Investigation investigation) {
		List<AssetEntity> assets = assetsRepository.findByIdIn(investigation.getAssetIds());

		if (!assets.isEmpty()) {
			InvestigationEntity savedInvestigation = investigationRepository.save(new InvestigationEntity(assets, investigation.getDescription(), investigation.getInvestigationStatus()));
			Map<String, List<AssetEntity>> manufacturerAssets = assets.stream()
				.collect(Collectors.groupingBy(AssetEntity::getManufacturerId));

			List<NotificationEntity> notifications = manufacturerAssets.entrySet().stream()
				.map((entry) -> new NotificationEntity(savedInvestigation, entry.getKey(), entry.getValue()))
				.toList();

			notificationRepository.saveAll(notifications);
		}
	}

	@Override
	public Investigation getInvestigation(Long investigationId) {
		return investigationRepository.findById(investigationId)
			.map(this::toInvestigation)
			.orElse(null);
	}

	@Override
	public void update(Investigation investigation) {
		InvestigationEntity investigationEntity = investigationRepository.findById(investigation.getId()).orElse(null);

		if (investigationEntity != null) {
			update(investigationEntity, investigation);
			investigationRepository.save(investigationEntity);
		}
	}

	private void update(InvestigationEntity investigationEntity, Investigation investigation) {
		investigationEntity.setStatus(investigation.getInvestigationStatus());

		investigationEntity.getNotifications().forEach(notification -> {
			investigation.getNotification(notification.getId()).ifPresent(data -> {
				notification.setEdcUrl(data.getEdcUrl());
			});
		});
	}

	private Investigation toInvestigation(InvestigationEntity investigationEntity) {
		List<Notification> notifications = investigationEntity.getNotifications().stream()
			.map(this::toNotification)
			.toList();

		List<String> assetIds = investigationEntity.getAssets().stream()
			.map(AssetEntity::getId)
			.toList();

		return new Investigation(investigationEntity.getId(), investigationEntity.getStatus(), investigationEntity.getDescription(), assetIds, notifications);
	}

	private Notification toNotification(NotificationEntity notificationEntity) {
		return new Notification(notificationEntity.getId(), notificationEntity.getBpnNumber(), notificationEntity.getEdcUrl());
	}
}
