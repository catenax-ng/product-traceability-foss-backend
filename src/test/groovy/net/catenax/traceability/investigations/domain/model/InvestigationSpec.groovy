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

package net.catenax.traceability.investigations.domain.model

import net.catenax.traceability.common.model.BPN
import net.catenax.traceability.investigations.domain.model.exception.InvestigationStatusTransitionNotAllowed
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant

import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.ACCEPTED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.ACKNOWLEDGED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.APPROVED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.CLOSED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.CONFIRMED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.CREATED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.DECLINED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.RECEIVED

class InvestigationSpec extends Specification {

	@Unroll
	def "should not allow to cancel investigation with #investigationStatus status"() {
		given:
			Investigation investigation = investigationWithStatus(investigationStatus)

		when:
			investigation.cancel()

		then:
			thrown(InvestigationStatusTransitionNotAllowed)

		and:
			investigation.getInvestigationStatus() == investigationStatus

		where:
			investigationStatus << [RECEIVED, ACKNOWLEDGED, DECLINED, CLOSED, CONFIRMED]
	}

	@Unroll
	def "should allow to cancel investigation with #investigationStatus status"() {
		given:
			Investigation investigation = investigationWithStatus(investigationStatus)

		when:
			investigation.cancel()

		then:
			noExceptionThrown()

		and:
			investigation.getInvestigationStatus() == CLOSED

		where:
			investigationStatus << [CREATED, APPROVED, ACCEPTED]
	}

	private Investigation investigationWithStatus(InvestigationStatus status) {
		new Investigation(new InvestigationId(1L), new BPN("1"), status, "", Instant.now(), [])
	}
}
