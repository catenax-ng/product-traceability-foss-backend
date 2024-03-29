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

package org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.negotiation;


import org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.message.RemoteMessage;

import java.util.Objects;

public class ContractRejection implements RemoteMessage {

	private String protocol;
	private String connectorId;
	private String connectorAddress;
	private String correlationId; // TODO hand over the contract offer/agreement - not an id?
	private String rejectionReason; // TODO pre-define a set of enums (+ mapping to IDS) ?

	@Override
	public String getProtocol() {
		return protocol;
	}


	public String getConnectorAddress() {
		return connectorAddress;
	}

	public String getConnectorId() {
		return connectorId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public static class Builder {
		private final ContractRejection contractRejection;

		private Builder() {
			this.contractRejection = new ContractRejection();
		}

		public static Builder newInstance() {
			return new Builder();
		}

		public Builder protocol(String protocol) {
			this.contractRejection.protocol = protocol;
			return this;
		}

		public Builder connectorId(String connectorId) {
			this.contractRejection.connectorId = connectorId;
			return this;
		}

		public Builder connectorAddress(String connectorAddress) {
			this.contractRejection.connectorAddress = connectorAddress;
			return this;
		}

		public Builder correlationId(String correlationId) {
			this.contractRejection.correlationId = correlationId;
			return this;
		}

		public Builder rejectionReason(String rejectionReason) {
			this.contractRejection.rejectionReason = rejectionReason;
			return this;
		}

		public ContractRejection build() {
			Objects.requireNonNull(contractRejection.protocol, "protocol");
			Objects.requireNonNull(contractRejection.connectorId, "connectorId");
			Objects.requireNonNull(contractRejection.connectorAddress, "connectorAddress");
			Objects.requireNonNull(contractRejection.correlationId, "correlationId");
			Objects.requireNonNull(contractRejection.rejectionReason, "rejectionReason");
			return contractRejection;
		}
	}
}
