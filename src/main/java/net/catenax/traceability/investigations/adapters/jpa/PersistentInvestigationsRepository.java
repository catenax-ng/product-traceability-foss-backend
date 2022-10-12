/********************************************************************************
 * Copyright (c) 2021,2022 Contributors to the CatenaX (ng) GitHub Organisation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package net.catenax.traceability.investigations.adapters.jpa;

import net.catenax.traceability.investigations.domain.model.InvestigationStatus;
import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.AssetEntity;
import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.JpaAssetsRepository;
import net.catenax.traceability.common.model.PageResult;
import net.catenax.traceability.infrastructure.jpa.investigation.InvestigationEntity;
import net.catenax.traceability.infrastructure.jpa.investigation.JpaInvestigationRepository;
import net.catenax.traceability.infrastructure.jpa.notification.JpaNotificationRepository;
import net.catenax.traceability.infrastructure.jpa.notification.NotificationEntity;
import net.catenax.traceability.investigations.domain.model.Investigation;
import net.catenax.traceability.investigations.domain.ports.InvestigationsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
	public PageResult<Investigation> getInvestigations(Set<InvestigationStatus> investigationStatuses, Pageable pageable) {
		Page<InvestigationEntity> entities = investigationRepository.findAllByStatusIn(investigationStatuses, pageable);

		return new PageResult<>(entities, this::toInvestigation);
	}

	private Investigation toInvestigation(InvestigationEntity investigationEntity) {
		return new Investigation(
			investigationEntity.getAssets().stream().map(AssetEntity::getId).toList(),
			investigationEntity.getStatus(),
			investigationEntity.getDescription(),
			investigationEntity.getCreated().toInstant()
		);
	}
}