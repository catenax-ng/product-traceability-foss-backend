package net.catenax.traceability.investigations.infrastructure.adapters.rest

import net.catenax.traceability.IntegrationSpec
import net.catenax.traceability.common.security.KeycloakRole
import org.springframework.http.MediaType

import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.not
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class InvestigationsControllerIT extends IntegrationSpec {

	def "should not create new investigation when user is not authorized"() {
		given:
			unauthenticatedUser()

		expect:
			mvc.perform(post("/investigations")
				.content(
					asJson(
						[
							partIds    : ["123"],
							description: "some-description"
						],
					)
				)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
	}

	def "should not create new investigation when description argument is invalid"() {
		given:
			authenticatedUser(KeycloakRole.ADMIN)

		expect:
			mvc.perform(post("/investigations")
				.content(
					asJson(
						[
							partIds    : ["123"],
							description: description
						],
					)
				)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())

		where:
			description << [null, "", " ", "    "]
	}

	def "should not create new investigation when part ids argument is invalid"() {
		given:
			authenticatedUser(KeycloakRole.ADMIN)

		expect:
			mvc.perform(post("/investigations")
				.content(
					asJson(
						[
							partIds    : partIds,
							description: "some-description"
						],
					)
				)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())

		where:
			partIds << [[], null]
	}

	def "should create new investigation for valid arguments"() {
		given:
			authenticatedUser(KeycloakRole.ADMIN)

		expect:
			mvc.perform(post("/investigations")
				.content(
					asJson(
						[
							partIds    : ["123"],
							description: "some-description"
						],
					)
				)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath('$.investigationId', not(empty())))
	}
}
