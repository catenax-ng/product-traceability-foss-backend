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

package net.catenax.traceability.assets.infrastructure.adapters.registry;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistryService {
	private final RegistryAdapter registryAdapter;
	private final ShellDescriptorStore shellDescriptorStore;

	public RegistryService(ShellDescriptorStore shellDescriptorStore, RegistryAdapter registryAdapter) {
		this.shellDescriptorStore = shellDescriptorStore;
		this.registryAdapter = registryAdapter;
	}

	public void loadShellDescriptorsFor(String bpn) {
		List<ShellDescriptor> descriptors = registryAdapter.findAssetsByBpn(bpn);
		shellDescriptorStore.store(descriptors);
	}
}
