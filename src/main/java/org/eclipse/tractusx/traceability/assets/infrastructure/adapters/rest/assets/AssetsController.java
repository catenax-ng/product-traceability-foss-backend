/********************************************************************************
 * Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 * Copyright (c) 2022, 2023 ZF Friedrichshafen AG
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.traceability.assets.infrastructure.adapters.rest.assets;

import org.eclipse.tractusx.traceability.assets.application.AssetFacade;
import org.eclipse.tractusx.traceability.assets.domain.model.Asset;
import org.eclipse.tractusx.traceability.assets.domain.ports.AssetRepository;
import org.eclipse.tractusx.traceability.common.model.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_USER')")
public class AssetsController {

	private final AssetRepository assetRepository;
	private final AssetFacade assetFacade;

	public AssetsController(AssetRepository assetRepository, AssetFacade assetFacade) {
		this.assetRepository = assetRepository;
		this.assetFacade = assetFacade;
	}

	@PostMapping("/assets/sync")
	public void sync(@Valid @RequestBody SyncAssets syncAssets) {
		assetFacade.synchronizeAssetsAsync(syncAssets.globalAssetIds());
	}

	@GetMapping("/assets")
	public PageResult<Asset> assets(Pageable pageable) {
		return assetRepository.getAssets(pageable);
	}

	@GetMapping("/assets/supplier")
	public PageResult<Asset> supplierAssets(Pageable pageable) {
		return assetRepository.getSupplierAssets(pageable);
	}

	@GetMapping("/assets/my")
	public PageResult<Asset> ownAssets(Pageable pageable) {
		return assetRepository.getOwnAssets(pageable);
	}

	@GetMapping("/assets/countries")
	public Map<String, Long> assetsCountryMap() {
		return assetFacade.getAssetsCountryMap();
	}

	@GetMapping("/assets/{assetId}")
	public Asset asset(@PathVariable String assetId) {
		return assetRepository.getAssetById(assetId);
	}

	@GetMapping("/assets/{assetId}/children/{childId}")
	public Asset asset(@PathVariable String assetId, @PathVariable String childId) {
		return assetRepository.getAssetByChildId(assetId, childId);
	}

	@PatchMapping("/assets/{assetId}")
	public Asset updateAsset(@PathVariable String assetId, @Valid @RequestBody UpdateAsset updateAsset) {
		return assetFacade.updateAsset(assetId, updateAsset);
	}

	@PostMapping("/assets/detail-information")
	public List<Asset> getDetailInformation(@Valid @RequestBody GetDetailInformationRequest getDetailInformationRequest) {
		return assetRepository.getAssetsById(getDetailInformationRequest.assetIds());
	}
}
