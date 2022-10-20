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

import java.util.Collections;
import java.util.Map;
import java.util.Set;


public enum InvestigationStatus {
	CREATED,
	APPROVED,
	SENT,
	RECEIVED,
	ACKNOWLEDGED,
	ACCEPTED,
	DECLINED,
	CLOSED,
	CONFIRMED;

	private static final Map<InvestigationStatus, Set<InvestigationStatus>> STATE_MACHINE;

	private static final Set<InvestigationStatus> NO_TRANSITION_ALLOWED = Collections.emptySet();

	static {
		STATE_MACHINE = Map.of(
			CREATED, Set.of(APPROVED, CLOSED),
			APPROVED, Set.of(CLOSED, SENT),
			SENT, NO_TRANSITION_ALLOWED,
			RECEIVED, Set.of(ACKNOWLEDGED),
			ACKNOWLEDGED, Set.of(ACCEPTED),
			ACCEPTED, Set.of(CONFIRMED, CLOSED),
			DECLINED, NO_TRANSITION_ALLOWED,
			CLOSED, NO_TRANSITION_ALLOWED,
			CONFIRMED, NO_TRANSITION_ALLOWED
		);
	}

	public boolean transitionAllowed(InvestigationStatus to) {
		return STATE_MACHINE.get(this).contains(to);
	}
}
