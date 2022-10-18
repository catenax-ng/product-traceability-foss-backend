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

import io.restassured.http.ContentType
import net.catenax.traceability.IntegrationSpecification
import net.catenax.traceability.assets.domain.model.Asset
import net.catenax.traceability.assets.domain.model.InvestigationStatus
import net.catenax.traceability.common.support.AssetsSupport
import net.catenax.traceability.common.support.IrsApiSupport
import net.catenax.traceability.infrastructure.jpa.investigation.InvestigationEntity
import net.catenax.traceability.infrastructure.jpa.investigation.JpaInvestigationRepository
import net.catenax.traceability.infrastructure.jpa.notification.JpaNotificationRepository
import net.catenax.traceability.infrastructure.jpa.notification.NotificationEntity
import org.springframework.beans.factory.annotation.Autowired

import static io.restassured.RestAssured.given
import static net.catenax.traceability.common.security.JwtRole.ADMIN

class InvestigationsControllerIT extends IntegrationSpecification implements IrsApiSupport, AssetsSupport {

	@Autowired
	JpaInvestigationRepository jpaInvestigationRepository

	@Autowired
	JpaNotificationRepository jpaNotificationRepository

	def "should start investigation"() {
		given:
			List<String> partIds = [
				"urn:uuid:fe99da3d-b0de-4e80-81da-882aebcca978", // BPN: BPNL00000003AYRE
				"urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2bb", // BPN: BPNL00000003AYRE
				"urn:uuid:0ce83951-bc18-4e8f-892d-48bad4eb67ef"  // BPN: BPNL00000003AXS3
			]
			String description = "at least 15 characters long investigation description"

		and:
			defaultAssetsStored()

		when:
			given()
				.contentType(ContentType.JSON)
				.body(
					asJson(
						[
							partIds    : partIds,
							description: description
						]
					)
				)
				.header(jwtAuthorization(ADMIN))
			.when()
				.post("/api/investigations")
			.then()
				.statusCode(200)

		then:
			partIds.each { partId ->
				Asset asset = assetRepository().getAssetById(partId)
				assert asset
				assert asset.isUnderInvestigation()
			}

		and:
			List<NotificationEntity> notifications = jpaNotificationRepository.findAll()
			notifications.size() == 2

		and:
			List<InvestigationEntity> investigations = jpaInvestigationRepository.findAll()
			investigations.size() == 1
	}

	def "should update investigation status"() {
		given:
			List<String> partIds = [
				"urn:uuid:fe99da3d-b0de-4e80-81da-882aebcca978", // BPN: BPNL00000003AYRE
				"urn:uuid:0ce83951-bc18-4e8f-892d-48bad4eb67ef"  // BPN: BPNL00000003AXS3
			]
			String description = "at least 15 characters long investigation description"
			InvestigationStatus status = InvestigationStatus.APPROVED

		and:
			defaultAssetsStored()

		when:
			given()
				.contentType(ContentType.JSON)
				.body(asJson([
						partIds    : partIds,
						description: description
				]))
				.header(jwtAuthorization(ADMIN))
			.when()
				.post("/api/investigations")
			.then()
				.statusCode(200)

		then:
			List<InvestigationEntity> investigations = jpaInvestigationRepository.findAll()
			investigations.size() == 1
			Long investigationId = investigations[0].id

		when:
			given()
				.contentType(ContentType.JSON)
				.body(asJson([
					status    : status
				]))
				.header(jwtAuthorization(ADMIN))
			.when()
				.put("/api/investigations/{investigationId}/status", investigationId)
			.then()
				.statusCode(200)

		then:
			eventually {
				List<NotificationEntity> notifications = jpaNotificationRepository.findAll()
				notifications.size() == 2
				notifications.each { notification ->
					assert notification.edcUrl != null
					assert notification.contractAgreementId != null
				}
			}
	}

}
