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

package net.catenax.traceability.common.security

import com.nimbusds.jose.shaded.json.JSONArray
import com.nimbusds.jose.shaded.json.JSONObject
import org.springframework.security.oauth2.jwt.Jwt
import spock.lang.Specification

import java.time.Instant

class KeycloakRolesExtractorSpec extends Specification {

	def "should extract keycloak resource client roles"() {
		given:
			String resourceClient = "unit-tests"

		and:
			Set<String> keycloakMappedRoles = roles
				.each { it -> it.description }
				.collect { it.description }

		and:
			Jwt token = createToken(keycloakMappedRoles, resourceClient)

		when:
			Set<KeycloakRole> extractedRoles = KeycloakRolesExtractor.extract(token, resourceClient)

		then:
			extractedRoles == roles

		where:
			_ | roles
			_ | [KeycloakRole.USER] as Set
			_ | [KeycloakRole.ADMIN] as Set
			_ | [KeycloakRole.SUPERVISOR] as Set
			_ | [KeycloakRole.USER, KeycloakRole.ADMIN, KeycloakRole.SUPERVISOR] as Set
	}

	def "should extract only mapped keycloak roles"() {
		given:
			String resourceClient = "unit-tests"

		and:
			Jwt token = createToken(roles.toSet(), resourceClient)

		when:
			Set<KeycloakRole> extractedRoles = KeycloakRolesExtractor.extract(token, resourceClient)

		then:
			extractedRoles == result

		where:
			roles                                                     | result
			["Unknown"]                                               | [] as Set
			["User", "someUnknownRole1", "Admin", "someUnknownRole2"] | [KeycloakRole.USER, KeycloakRole.ADMIN] as Set

	}

	def "shouldn't not extract from keycloak unknown resource client "() {
		given:
			Set<String> keycloakMappedRoles = roles
				.each { it -> it.description }
				.collect { it.description }

		and:
			Jwt token = createToken(keycloakMappedRoles, "unit-tests")

		when:
			Set<KeycloakRole> extractedRoles = KeycloakRolesExtractor.extract(token, "unknown-resource-client")

		then:
			extractedRoles.isEmpty()

		where:
			_ | roles
			_ | [KeycloakRole.USER] as Set
			_ | [KeycloakRole.ADMIN] as Set
			_ | [KeycloakRole.SUPERVISOR] as Set
			_ | [KeycloakRole.USER, KeycloakRole.ADMIN, KeycloakRole.SUPERVISOR] as Set
	}

	private static Jwt createToken(Set<String> roles, String resourceClient) {
		Jwt.Builder jwtBuilder = Jwt.withTokenValue("some-value")
			.issuer(UUID.randomUUID().toString())
			.subject(UUID.randomUUID().toString())
			.issuedAt(Instant.now())
			.expiresAt(Instant.now() + 60)
			.header("alg", "RS256")
			.header("use", "sig")
			.header("typ", "JWT")

		JSONArray jSONArray = new JSONArray()
		roles.each { it -> jSONArray.appendElement(it) }
		Object resourceAccess = [(resourceClient): new JSONObject(Map.of("roles", jSONArray))]
		jwtBuilder.claim("resource_access", resourceAccess)

		return jwtBuilder.build()
	}
}
