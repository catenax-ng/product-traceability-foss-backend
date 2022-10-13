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
import net.catenax.traceability.common.support.AssetsSupport
import net.catenax.traceability.common.support.InvestigationsSupport
import net.catenax.traceability.common.support.IrsApiSupport
import net.catenax.traceability.common.support.NotificationsSupport
import net.catenax.traceability.infrastructure.jpa.investigation.InvestigationEntity
import net.catenax.traceability.investigations.domain.model.InvestigationStatus
import org.hamcrest.Matchers

import java.time.ZonedDateTime

import static io.restassured.RestAssured.given
import static net.catenax.traceability.common.security.JwtRole.ADMIN

class InvestigationsControllerIT extends IntegrationSpecification implements IrsApiSupport, AssetsSupport, InvestigationsSupport, NotificationsSupport {

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
			assertNotificationsSize(2)

		and:
			given()
				.header(jwtAuthorization(ADMIN))
				.param("page", "0")
				.param("size", "10")
				.contentType(ContentType.JSON)
				.when()
				.get("/api/investigations/created")
				.then()
				.statusCode(200)
				.body("page", Matchers.is(0))
				.body("pageSize", Matchers.is(10))
				.body("content", Matchers.hasSize(1))
	}

	def "should not return investigations without authentication"() {
		expect:
			given()
				.param("page", "0")
				.param("size", "10")
				.contentType(ContentType.JSON)
				.when()
				.get("/api/investigations/$type")
				.then()
				.statusCode(401)
		where:
			type << ["created", "received"]
	}

	def "should return no investigations"() {
		expect:
			given()
				.header(jwtAuthorization(ADMIN))
				.param("page", "0")
				.param("size", "10")
				.contentType(ContentType.JSON)
				.when()
				.get("/api/investigations/$type")
				.then()
				.statusCode(200)
				.body("page", Matchers.is(0))
				.body("pageSize", Matchers.is(10))
				.body("content", Matchers.hasSize(0))

		where:
			type << ["created", "received"]
	}

	def "should return created investigations sorted by creation time"() {
		given:
			ZonedDateTime now = ZonedDateTime.now()

		and:
			storedInvestigations(
				new InvestigationEntity([], InvestigationStatus.CREATED, "1", now.minusSeconds(10L)),
				new InvestigationEntity([], InvestigationStatus.CREATED, "2", now.plusSeconds(21L)),
				new InvestigationEntity([], InvestigationStatus.CREATED, "3", now),
				new InvestigationEntity([], InvestigationStatus.CREATED, "4", now.plusSeconds(20L)),
				new InvestigationEntity([], InvestigationStatus.RECEIVED, "5", now.plusSeconds(40L))
			)

		expect:
			given()
				.header(jwtAuthorization(ADMIN))
				.param("page", "0")
				.param("size", "10")
				.contentType(ContentType.JSON)
				.when()
				.get("/api/investigations/created")
				.then()
				.statusCode(200)
				.body("page", Matchers.is(0))
				.body("pageSize", Matchers.is(10))
				.body("content", Matchers.hasSize(4))
				.body("content.description", Matchers.containsInRelativeOrder("2", "4", "3", "1"))
	}

	def "should return received investigations sorted by creation time"() {
		given:
			ZonedDateTime now = ZonedDateTime.now()

		and:
			storedInvestigations(
				new InvestigationEntity([], InvestigationStatus.RECEIVED, "1", now.minusSeconds(5L)),
				new InvestigationEntity([], InvestigationStatus.RECEIVED, "2", now.plusSeconds(2L)),
				new InvestigationEntity([], InvestigationStatus.RECEIVED, "3", now),
				new InvestigationEntity([], InvestigationStatus.RECEIVED, "4", now.plusSeconds(20L)),
				new InvestigationEntity([], InvestigationStatus.CREATED, "5", now.plusSeconds(40L))
			)

		expect:
			given()
				.header(jwtAuthorization(ADMIN))
				.param("page", "0")
				.param("size", "10")
				.contentType(ContentType.JSON)
				.when()
				.get("/api/investigations/received")
				.then()
				.statusCode(200)
				.body("page", Matchers.is(0))
				.body("pageSize", Matchers.is(10))
				.body("content", Matchers.hasSize(4))
				.body("content.description", Matchers.containsInRelativeOrder("4", "2", "3", "1"))
	}

	def "should not return investigation without authentication"() {
		expect:
			given()
				.contentType(ContentType.JSON)
				.when()
				.get("/api/investigations/123")
				.then()
				.statusCode(401)
	}

	def "should not find non existing investigation"() {
		expect:
			given()
				.header(jwtAuthorization(ADMIN))
				.contentType(ContentType.JSON)
				.when()
				.get("/api/investigations/1234")
				.then()
				.statusCode(404)
				.body("message", Matchers.is("Investigation not found for 1234 id"))
	}

	def "should return investigation by id"() {
		given:
			Long investigationId = storedInvestigation(new InvestigationEntity([], "1", InvestigationStatus.RECEIVED))

		expect:
			given()
				.header(jwtAuthorization(ADMIN))
				.contentType(ContentType.JSON)
				.when()
				.get("/api/investigations/$investigationId")
				.then()
				.statusCode(200)
				.body("id", Matchers.is(investigationId.toInteger()))
				.body("status", Matchers.is("RECEIVED"))
				.body("description", Matchers.is("1"))
				.body("assetIds", Matchers.empty())
	}
}
