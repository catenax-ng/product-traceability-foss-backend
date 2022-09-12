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

package net.catenax.traceability.openapi

import net.catenax.traceability.IntegrationSpec
import net.catenax.traceability.common.security.KeycloakRole
import org.apache.commons.io.FileUtils
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class OpenAPIDocumentation extends IntegrationSpec {

	private static final String DOCUMENTATION_FILENAME = "./openapi/product-traceability-foss-backend.json"

	def "should generate openapi documentation"() {
		given:
			authenticatedUser(KeycloakRole.ADMIN)

		expect:
			mvc.perform(get("/v3/api-docs").accept(MediaType.APPLICATION_JSON))
				.andDo(result -> {
					FileUtils.writeStringToFile(new File(DOCUMENTATION_FILENAME), result.getResponse().getContentAsString())
				})
				.andExpect(status().isOk())
	}
}
