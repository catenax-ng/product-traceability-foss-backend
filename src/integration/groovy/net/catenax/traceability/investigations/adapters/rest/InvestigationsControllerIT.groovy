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

package net.catenax.traceability.investigations.adapters.rest

import net.catenax.traceability.IntegrationSpecification
import net.catenax.traceability.assets.domain.model.Asset
import net.catenax.traceability.investigations.domain.model.InvestigationStatus
import net.catenax.traceability.common.security.KeycloakRole
import net.catenax.traceability.common.support.AssetsSupport
import net.catenax.traceability.common.support.IrsApiSupport
import net.catenax.traceability.infrastructure.jpa.investigation.InvestigationEntity
import org.hamcrest.Matchers
import org.springframework.http.MediaType

import java.time.ZonedDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class InvestigationsControllerIT extends IntegrationSpecification implements IrsApiSupport, AssetsSupport {

	def "should start investigation"() {
		given:
			List<String> partIds = [
				"urn:uuid:fe99da3d-b0de-4e80-81da-882aebcca978", // BPN: BPNL00000003AYRE
				"urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2bb", // BPN: BPNL00000003AYRE
				"urn:uuid:0ce83951-bc18-4e8f-892d-48bad4eb67ef"  // BPN: BPNL00000003AXS3
			]
			String description = "at least 15 characters long investigation description"
			authenticatedUser(KeycloakRole.ADMIN)

		and:
			defaultAssetsStored()

		when:
			mvc.perform(post("/investigations")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJson(
					[
						partIds    : partIds,
						description: description
					]
				))
			).andExpect(status().isOk())

		then:
			partIds.each { partId ->
				Asset asset = assetRepository().getAssetById(partId)
				assert asset
				assert asset.isUnderInvestigation()
			}

		and:
			assertNotificationsSize(2)

		and:
			mvc.perform(get("/investigations/created")
				.queryParam("page", "0")
				.queryParam("size", "10")
				.contentType(MediaType.APPLICATION_JSON)
			)
				.andExpect(status().isOk())
				.andExpect(jsonPath('$.page', Matchers.is(0)))
				.andExpect(jsonPath('$.pageSize', Matchers.is(10)))
				.andExpect(jsonPath('$.content', Matchers.hasSize(1)))
	}

	def "should not return investigations without authentication"() {
		given:
			unauthenticatedUser()

		expect:
			mvc.perform(get("/investigations")
				.queryParam("page", "0")
				.queryParam("size", "2")
				.contentType(MediaType.APPLICATION_JSON)
			).andExpect(status().isUnauthorized())
	}

	def "should return no investigations"() {
		given:
			authenticatedUser(KeycloakRole.ADMIN)

		expect:
			mvc.perform(get("/investigations/$type")
				.queryParam("page", "1")
				.queryParam("size", "10")
				.contentType(MediaType.APPLICATION_JSON)
			)
				.andExpect(status().isOk())
				.andExpect(jsonPath('$.page', Matchers.is(0)))
				.andExpect(jsonPath('$.pageSize', Matchers.is(10)))
				.andExpect(jsonPath('$.content', Matchers.hasSize(0)))

		where:
			type << ["created", "received"]
	}

	def "should return created investigations sorted by creation time"() {
		given:
			authenticatedUser(KeycloakRole.ADMIN)

		and:
			ZonedDateTime now = ZonedDateTime.now()

			storedInvestigations(
				new InvestigationEntity([], InvestigationStatus.CREATED, "1", now.minusSeconds(10L)),
				new InvestigationEntity([], InvestigationStatus.CREATED, "2", now.plusSeconds(21L)),
				new InvestigationEntity([], InvestigationStatus.CREATED, "3", now),
				new InvestigationEntity([], InvestigationStatus.CREATED, "4", now.plusSeconds(20L)),
				new InvestigationEntity([], InvestigationStatus.RECEIVED, "5", now.plusSeconds(40L))
			)

		expect:
			mvc.perform(get("/investigations/created")
				.queryParam("page", "0")
				.queryParam("size", "10")
				.contentType(MediaType.APPLICATION_JSON)
			)
				.andExpect(status().isOk())
				.andExpect(jsonPath('$.page', Matchers.is(0)))
				.andExpect(jsonPath('$.pageSize', Matchers.is(10)))
				.andExpect(jsonPath('$.content', Matchers.hasSize(4)))
				.andExpect(jsonPath('$.content.*.description', Matchers.containsInRelativeOrder("2", "4", "3", "1")))
	}

	def "should return received investigations sorted by creation time"() {
		given:
			authenticatedUser(KeycloakRole.ADMIN)

		and:
			ZonedDateTime now = ZonedDateTime.now()

			storedInvestigations(
				new InvestigationEntity([], InvestigationStatus.RECEIVED, "1", now.minusSeconds(5L)),
				new InvestigationEntity([], InvestigationStatus.RECEIVED, "2", now.plusSeconds(2L)),
				new InvestigationEntity([], InvestigationStatus.RECEIVED, "3", now),
				new InvestigationEntity([], InvestigationStatus.RECEIVED, "4", now.plusSeconds(20L)),
				new InvestigationEntity([], InvestigationStatus.CREATED, "5", now.plusSeconds(40L))
			)

		expect:
			mvc.perform(get("/investigations/received")
				.queryParam("page", "0")
				.queryParam("size", "10")
				.contentType(MediaType.APPLICATION_JSON)
			)
				.andExpect(status().isOk())
				.andExpect(jsonPath('$.page', Matchers.is(0)))
				.andExpect(jsonPath('$.pageSize', Matchers.is(10)))
				.andExpect(jsonPath('$.content', Matchers.hasSize(4)))
				.andExpect(jsonPath('$.content.*.description', Matchers.containsInRelativeOrder("4", "2", "3", "1")))
	}
}
