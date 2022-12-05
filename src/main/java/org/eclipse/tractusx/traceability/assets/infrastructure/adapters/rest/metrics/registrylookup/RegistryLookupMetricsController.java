/********************************************************************************
 * Copyright (c) 2022,2023
 *        2022: Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *        2022: ZF Friedrichshafen AG
 * Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
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


package org.eclipse.tractusx.traceability.assets.infrastructure.adapters.rest.metrics.registrylookup;

import org.eclipse.tractusx.traceability.assets.infrastructure.adapters.metrics.RegistryLookupMeterRegistry;
import org.eclipse.tractusx.traceability.assets.infrastructure.adapters.metrics.RegistryLookupMetric;
import org.eclipse.tractusx.traceability.common.model.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERVISOR')")
public class RegistryLookupMetricsController {

	private final RegistryLookupMeterRegistry registryLookupMeterRegistry;

	public RegistryLookupMetricsController(RegistryLookupMeterRegistry registryLookupMeterRegistry) {
		this.registryLookupMeterRegistry = registryLookupMeterRegistry;
	}

	@GetMapping("/registry-lookup")
	public PageResult<RegistryLookupMetric> metrics(Pageable pageable) {
		return registryLookupMeterRegistry.getMetrics(pageable);
	}
}
