/*
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
 */

package net.catenax.traceability.assets.domain;

import net.catenax.traceability.assets.infrastructure.adapters.registry.RegistryAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistryService {
	private final RegistryAdapter registryAdapter;
	private final ShellDescriptorStore shellDescriptorStore;
	private final AssetService assetService;

	@Value("${feign.registryApi.defaultBpn}")
	private String defaultBpn;

	public RegistryService(ShellDescriptorStore shellDescriptorStore, RegistryAdapter registryAdapter, AssetService assetService) {
		this.shellDescriptorStore = shellDescriptorStore;
		this.registryAdapter = registryAdapter;
		this.assetService = assetService;
	}

	@Async
	@Scheduled(cron = "0 0 */2 * * ?", zone = "Europe/Berlin")
	void schedule() {
		loadShellDescriptorsFor(defaultBpn);
	}

	@Async
	public void loadShellDescriptorsFor(String bpn) {
		List<ShellDescriptor> descriptors = registryAdapter.findAssetsByBpn(bpn);

		// we do not have a proper update mechanism at the moment
		shellDescriptorStore.deleteAll();
		shellDescriptorStore.store(descriptors);

		List<String> globalAssetIds = descriptors.stream().map(ShellDescriptor::globalAssetId).toList();

		// add list
		assetService.synchronizeAssets(globalAssetIds);
	}
}
