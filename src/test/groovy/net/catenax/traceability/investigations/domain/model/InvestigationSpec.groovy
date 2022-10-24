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
import net.catenax.traceability.investigations.domain.model.exception.InvestigationIllegalUpdate
import net.catenax.traceability.investigations.domain.model.exception.InvestigationStatusTransitionNotAllowed
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant

import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.ACCEPTED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.ACKNOWLEDGED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.APPROVED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.CLOSED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.CREATED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.DECLINED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.RECEIVED
import static net.catenax.traceability.investigations.domain.model.InvestigationStatus.SENT

class InvestigationSpec extends Specification {

	@Unroll
	def "should not allow to cancel investigation with #investigationStatus status"() {
		given:
			BPN bpn = new BPN("BPNL000000000001")

		and:
			Investigation investigation = investigationWithStatus(bpn, investigationStatus)

		when:
			investigation.cancel(bpn)

		then:
			thrown(InvestigationStatusTransitionNotAllowed)

		and:
			investigation.getInvestigationStatus() == investigationStatus

		where:
			investigationStatus << [RECEIVED, ACKNOWLEDGED, DECLINED, ACCEPTED, CLOSED]
	}

	def "should not allow to cancel investigation for different bpn"() {
		given:
			BPN bpn = new BPN("BPNL000000000001")

		and:
			Investigation investigation = investigationWithStatus(bpn, investigationStatus)

		when:
			investigation.cancel(new BPN("BPNL000000000002"))

		then:
			thrown(InvestigationIllegalUpdate)

		and:
			investigation.getInvestigationStatus() == investigationStatus

		where:
			investigationStatus << InvestigationStatus.values().toList()
	}

	@Unroll
	def "should allow to cancel investigation with #investigationStatus status"() {
		given:
			BPN bpn = new BPN("BPNL000000000001")

		and:
			Investigation investigation = investigationWithStatus(bpn, investigationStatus)

		when:
			investigation.cancel(bpn)

		then:
			noExceptionThrown()

		and:
			investigation.getInvestigationStatus() == CLOSED

		where:
			investigationStatus << [CREATED, APPROVED, SENT]
	}

	private Investigation investigationWithStatus(BPN bpn, InvestigationStatus status) {
		new Investigation(new InvestigationId(1L), bpn, status, "", Instant.now(), [], [])
	}
}