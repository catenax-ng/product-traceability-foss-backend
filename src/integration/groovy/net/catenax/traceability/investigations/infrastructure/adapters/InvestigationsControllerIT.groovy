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

package net.catenax.traceability.investigations.infrastructure.adapters

import net.catenax.traceability.IntegrationSpec
import net.catenax.traceability.assets.domain.model.Asset
import net.catenax.traceability.assets.domain.model.InvestigationStatus
import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.AssetEntity
import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.JpaAssetsRepository
import net.catenax.traceability.common.security.KeycloakRole
import net.catenax.traceability.common.support.AssetsSupport
import net.catenax.traceability.common.support.IrsApiSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class InvestigationsControllerIT extends IntegrationSpec implements IrsApiSupport, AssetsSupport {

	@Autowired
	JpaAssetsRepository jpaAssetsRepository

	def "should start investigation"() {
		given:
			List<String> partIds = [
				"urn:uuid:fe99da3d-b0de-4e80-81da-882aebcca978",
				"urn:uuid:0ce83951-bc18-4e8f-892d-48bad4eb67ef"
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
						partIds: partIds,
						description: description
					]
				))
			).andExpect(status().isOk())

		then:
			List<AssetEntity> parts = jpaAssetsRepository.findByIdIn(partIds)
			parts.size() == 2
			parts.each { part ->
				assert part.investigations
				assert part.investigations[0].status == InvestigationStatus.PENDING
				assert part.investigations[0].description == description
			}

		and:
			partIds.each {partId ->
				Asset asset = assetRepository().getAssetById(partId)
				assert asset
				assert asset.isUnderInvestigation()
			}
	}

}
