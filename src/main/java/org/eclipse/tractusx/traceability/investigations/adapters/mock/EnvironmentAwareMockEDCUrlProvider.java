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

package org.eclipse.tractusx.traceability.investigations.adapters.mock;

import org.eclipse.tractusx.traceability.investigations.domain.ports.EDCUrlProvider;
import org.eclipse.tractusx.traceability.common.config.ApplicationProfiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Profile(ApplicationProfiles.NOT_TESTS)
public class EnvironmentAwareMockEDCUrlProvider implements EDCUrlProvider {

	private static final Logger logger = LoggerFactory.getLogger(EnvironmentAwareMockEDCUrlProvider.class);

	private static final String ENVIRONMENT_PLACEHOLDER = "{environment}";

	private static final String DEFAULT_EDC_URL = "https://trace-x-test-edc.%s.demo.catena-x.net".formatted(ENVIRONMENT_PLACEHOLDER);

	private final String senderBpn;

	private final String applicationEnvironment;

	private final BpnToEDCProviderMappings bpnToEDCProviderMappings;

	public EnvironmentAwareMockEDCUrlProvider(@Value("${traceability.bpn}") String senderBpn, @Autowired BpnToEDCProviderMappings bpnToEDCProviderMappings, @Autowired Environment applicationEnvironment) {
		this.senderBpn = senderBpn;
		this.bpnToEDCProviderMappings = bpnToEDCProviderMappings;
		this.applicationEnvironment = Arrays.stream(applicationEnvironment.getActiveProfiles())
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("No environment found"));
	}

	public String getEdcUrl(String bpn) {
		String edcUrl = bpnToEDCProviderMappings.getBpnProviderUrlMappings().getOrDefault(bpn, defaultProviderUrl());

		logger.info("Resolved {} url for {} bpn", edcUrl, bpn);

		return edcUrl;
	}

	private String defaultProviderUrl() {
		return DEFAULT_EDC_URL.replace(ENVIRONMENT_PLACEHOLDER, applicationEnvironment);
	}

	@Override
	public String getSenderUrl() {
		return getEdcUrl(senderBpn);
	}

	public String getSenderBpn() {
		return senderBpn;
	}
}
