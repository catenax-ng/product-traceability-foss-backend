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

package net.catenax.traceability.investigations.domain.model;

import java.time.Clock;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Investigation {

	public static final Comparator<Investigation> COMPARE_BY_NEWEST_INVESTIGATION_CREATION_TIME = (o1, o2) -> {
		Instant o1CreationTime = o1.createdAt;
		Instant o2CreationTime = o2.createdAt;

		if (o1CreationTime.equals(o2CreationTime)) {
			return 0;
		}

		if (o1CreationTime.isBefore(o2CreationTime)) {
			return 1;
		}

		return -1;
	};

	public static final Set<InvestigationStatus> CREATED_STATUSES = Set.of(InvestigationStatus.CREATED, InvestigationStatus.APPROVED, InvestigationStatus.SENT);
	public static final Set<InvestigationStatus> RECEIVED_STATUSES = Set.of(InvestigationStatus.RECEIVED);

	private final List<String> assetIds;
	private final InvestigationStatus investigationStatus;
	private final Instant createdAt;
	private final String description;

	public Investigation(List<String> assetIds, InvestigationStatus investigationStatus, String description, Instant createdAt) {
		this.assetIds = assetIds;
		this.investigationStatus = investigationStatus;
		this.createdAt = createdAt;
		this.description = description;
	}

	public static Investigation startInvestigation(Clock clock, List<String> assetIds, String description) {
		return new Investigation(assetIds, InvestigationStatus.CREATED, description, clock.instant());
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
}
