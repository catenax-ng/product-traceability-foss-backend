package net.catenax.traceability.api

import net.catenax.traceability.IntegrationSpec
import org.hamcrest.Matchers
import org.springframework.http.MediaType

import static org.hamcrest.Matchers.hasItems
import static org.hamcrest.Matchers.nullValue
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TraceabilityControllerIT extends IntegrationSpec {

	def "should return assets for authenticated user with role"() {
		given:
			authenticatedUser(KeycloakRole.UMA_ROLE)

		expect:
			mvc.perform(get("/api/assets/1234").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
	}

	def "should return assets with manufacturer name"() {
		given:
			authenticatedUser(KeycloakRole.UMA_ROLE)
			keycloakApiReturnsToken()

		and:
			bpnApiReturnsBusinessPartnerDataFor(
				"BPNL000000000001",
				"BPNL00000003AXS3",
				"BPNL00000003B3NX",
				"BPNL00000003B5MJ"
			)

		expect:
			mvc.perform(get("/api/assets").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath('$.content[*].manufacturerName').isNotEmpty())

		and:
			verifyKeycloakApiCalledOnceForToken()
			verifyBpnApiCalledForBusinessPartnerDetails(4)
	}

	def "should return assets with manufacturer name using values from cache"() {
		given:
			authenticatedUser(KeycloakRole.UMA_ROLE)
			keycloakApiReturnsToken()

		and:
			bpnApiReturnsBusinessPartnerDataFor(
				"BPNL000000000001",
				"BPNL00000003AXS3",
				"BPNL00000003B3NX",
				"BPNL00000003B5MJ"
			)

		when:
			0..3.each {
				mvc.perform(get("/api/assets").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath('$.content[*].manufacturerName').isNotEmpty())
			}

		then:
			verifyKeycloakApiCalledOnceForToken()
			verifyBpnApiCalledForBusinessPartnerDetails(4)
	}

	def "should return assets without manufacturer name when name was not returned by BPN API"() {
		given:
			authenticatedUser(KeycloakRole.UMA_ROLE)
			keycloakApiReturnsToken()

		and:
			bpnApiReturnsBusinessPartnerDataWithoutNamesFor(
				"BPNL000000000001",
				"BPNL00000003AXS3",
				"BPNL00000003B3NX",
				"BPNL00000003B5MJ"
			)

		expect:
			mvc.perform(get("/api/assets").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath('$.content[*].manufacturerName', hasItems(nullValue())))
	}

	def "should return assets without manufacturer name when BPN API has no data"() {
		given:
			authenticatedUser(KeycloakRole.UMA_ROLE)
			keycloakApiReturnsToken()

		and:
			bpnApiReturnsNoBusinessPartnerDataFor(
				"BPNL000000000001",
				"BPNL00000003AXS3",
				"BPNL00000003B3NX",
				"BPNL00000003B5MJ"
			)

		expect:
			mvc.perform(get("/api/assets").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath('$.content[*].manufacturerName', hasItems(nullValue())))
	}

	def "should not return assets when user is not authenticated"() {
		given:
			unauthenticatedUser()

		expect:
			mvc.perform(get("/api/assets/1234").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
	}

	def "should get a page of assets"() {
		given:
			authenticatedUser(KeycloakRole.UMA_ROLE)

		expect:
			mvc.perform(get("/api/assets")
					.queryParam("page", "2")
					.queryParam("size", "2")
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath('$.page', Matchers.is(2)))
				.andExpect(jsonPath('$.pageSize', Matchers.is(2)))
	}
}
