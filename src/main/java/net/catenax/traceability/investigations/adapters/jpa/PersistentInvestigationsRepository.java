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

import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.AssetEntity;
import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.JpaAssetsRepository;
import net.catenax.traceability.common.model.BPN;
import net.catenax.traceability.common.model.PageResult;
import net.catenax.traceability.infrastructure.jpa.investigation.InvestigationEntity;
import net.catenax.traceability.infrastructure.jpa.investigation.JpaInvestigationRepository;
import net.catenax.traceability.infrastructure.jpa.notification.JpaNotificationRepository;
import net.catenax.traceability.infrastructure.jpa.notification.NotificationEntity;
import net.catenax.traceability.investigations.domain.model.Investigation;
import net.catenax.traceability.investigations.domain.model.InvestigationId;
import net.catenax.traceability.investigations.domain.model.InvestigationStatus;
import net.catenax.traceability.investigations.domain.ports.InvestigationsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PersistentInvestigationsRepository implements InvestigationsRepository {

	private final JpaInvestigationRepository investigationRepository;

	private final JpaAssetsRepository assetsRepository;

	private final JpaNotificationRepository notificationRepository;

	private final Clock clock;

	public PersistentInvestigationsRepository(JpaInvestigationRepository investigationRepository,
											  JpaAssetsRepository assetsRepository,
											  JpaNotificationRepository notificationRepository,
											  Clock clock) {
		this.investigationRepository = investigationRepository;
		this.assetsRepository = assetsRepository;
		this.notificationRepository = notificationRepository;
		this.clock = clock;
	}

	@Override
	public InvestigationId save(Investigation investigation) {
		if (investigation.hasIdentity()) {
			return merge(investigation);
		} else {
			return saveNew(investigation);
		}
	}

	private InvestigationId merge(Investigation investigation) {
		List<AssetEntity> assets = assetsRepository.findByIdIn(investigation.getAssetIds());

		InvestigationEntity investigationEntity = new InvestigationEntity(
			investigation.getId().value(),
			assets,
			investigation.getBpn(),
			investigation.getInvestigationStatus(),
			investigation.getDescription(),
			investigation.getCreationTime(),
			clock.instant()
		);

		investigationRepository.save(investigationEntity);

		return investigation.getId();
	}

	private InvestigationId saveNew(Investigation investigation) {
		List<String> assetIds = investigation.getAssetIds();

		List<AssetEntity> assets = assetsRepository.findByIdIn(assetIds);

		if (!assets.isEmpty()) {
			InvestigationEntity investigationEntity = new InvestigationEntity(assets, investigation.getBpn(), investigation.getDescription(), investigation.getInvestigationStatus(), investigation.getCreationTime());
			InvestigationEntity savedInvestigation = investigationRepository.save(investigationEntity);

			Map<String, List<AssetEntity>> manufacturerAssets = assets.stream()
				.collect(Collectors.groupingBy(AssetEntity::getManufacturerId));

			List<NotificationEntity> notifications = manufacturerAssets.entrySet().stream()
				.map((entry) -> new NotificationEntity(savedInvestigation, entry.getKey(), entry.getValue()))
				.toList();

			notificationRepository.saveAll(notifications);

			return new InvestigationId(savedInvestigation.getId());
		} else {
			throw new IllegalArgumentException("No assets found for %s asset ids".formatted(String.join(", ", assetIds)));
		}
	}

	@Override
	public PageResult<Investigation> getInvestigations(Set<InvestigationStatus> investigationStatuses, Pageable pageable) {
		Page<InvestigationEntity> entities = investigationRepository.findAllByStatusIn(investigationStatuses, pageable);

		return new PageResult<>(entities, this::toInvestigation);
	}

	@Override
	public Optional<Investigation> findById(InvestigationId investigationId) {
		return investigationRepository.findById(investigationId.value())
			.map(this::toInvestigation);
	}

	private Investigation toInvestigation(InvestigationEntity investigationEntity) {
		return new Investigation(
			new InvestigationId(investigationEntity.getId()),
			new BPN(investigationEntity.getBpn()),
			investigationEntity.getStatus(),
			investigationEntity.getDescription(),
			investigationEntity.getCreated(),
			investigationEntity.getAssets().stream().map(AssetEntity::getId).toList()
		);
	}
}
