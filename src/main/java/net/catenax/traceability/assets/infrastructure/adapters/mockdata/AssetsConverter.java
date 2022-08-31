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

package net.catenax.traceability.assets.infrastructure.adapters.mockdata;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.traceability.assets.domain.model.Asset;
import net.catenax.traceability.assets.domain.model.Asset.ChildDescriptions;
import net.catenax.traceability.assets.domain.model.QualityType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.catenax.traceability.assets.infrastructure.adapters.feign.irs.IRSApiClient.JobResponse;
import static net.catenax.traceability.assets.infrastructure.adapters.feign.irs.IRSApiClient.Shell;
import static net.catenax.traceability.assets.infrastructure.adapters.feign.irs.IRSApiClient.Submodel;

@Component
public class AssetsConverter {

	private static final String EMPTY_TEXT = "--";

	private final ObjectMapper mapper = new ObjectMapper()
		.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public List<Asset> readAndConvertAssets() {
		try {
			InputStream file = AssetsConverter.class.getResourceAsStream("/data/irs_assets.json");
			JobResponse response = mapper.readValue(file, JobResponse.class);

			return convertAssets(response);
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

	public List<Asset> convertAssets(JobResponse response)  {
		try {
			List<SerialPartTypization> parts = new ArrayList<>();
			Map<String, AssemblyPartRelationship> relationships = new HashMap<>();
			Map<String, String> shortIds = response.shells().stream()
				.collect(Collectors.toMap(Shell::identification, Shell::idShort));

			for (Submodel submodel : response.submodels()) {
				if (submodel.aspectType().contains("serial_part_typization")) {
					parts.add(mapper.readValue(submodel.payload(), SerialPartTypization.class));
				}
				if (submodel.aspectType().contains("assembly_part_relationship")) {
					AssemblyPartRelationship assemblyPartRelationship = mapper.readValue(submodel.payload(), AssemblyPartRelationship.class);
					relationships.put(assemblyPartRelationship.catenaXId, assemblyPartRelationship);
				}
			}

			return parts.stream()
				.map(part -> new Asset(
					part.catenaXId,
					shortIds.get(part.catenaXId),
					defaultValue(part.partTypeInformation.nameAtManufacturer),
					defaultValue(part.partTypeInformation.manufacturerPartID),
					part.manufacturerId(),
					EMPTY_TEXT,
					defaultValue(part.partTypeInformation.nameAtCustomer),
					defaultValue(part.partTypeInformation.customerPartId),
					part.manufacturingDate(),
					part.manufacturingCountry(),
					Collections.emptyMap(),
					getChildParts(relationships, shortIds, part.catenaXId),
					QualityType.OK
				)).toList();
		} catch (JsonProcessingException e) {
			return Collections.emptyList();
		}
	}

	private String defaultValue(String value) {
		if (StringUtils.isBlank(value)) {
			return EMPTY_TEXT;
		}
		return value;
	}

	private List<ChildDescriptions> getChildParts(Map<String, AssemblyPartRelationship> relationships, Map<String, String> shortIds, String catenaXId) {
		return Optional.ofNullable(relationships.get(catenaXId))
			.map(assemblyPartRelationship -> assemblyPartRelationship.childParts.stream()
				.map(child -> new ChildDescriptions(child.childCatenaXId(), shortIds.get(child.childCatenaXId)))
				.toList()
			).orElse(Collections.emptyList());
	}

	public record PartTypeInformation(
		String nameAtManufacturer,
		String nameAtCustomer,
		String manufacturerPartID,
		String customerPartId
	) {}

	public record ChildPart(
		String childCatenaXId
	) {}

	public record SerialPartTypization(
		String catenaXId,
		PartTypeInformation partTypeInformation,
		ManufacturingInformation manufacturingInformation,
		List<LocalId> localIdentifiers
	) {

		public String manufacturerId() {
			if (localIdentifiers == null) {
				return EMPTY_TEXT;
			}
			return localIdentifiers.stream()
				.filter(localId -> localId.type == LocalIdType.MANUFACTURER_ID)
				.findFirst()
				.map(LocalId::value)
				.orElse(EMPTY_TEXT);
		}

		public String manufacturingCountry() {
			if (manufacturingInformation == null) {
				return EMPTY_TEXT;
			}
			return manufacturingInformation.country;
		}

		public Instant manufacturingDate() {
			if (manufacturingInformation == null) {
				return null;
			}
			return Optional.ofNullable(manufacturingInformation.date)
				.map(Date::toInstant)
				.orElse(null);
		}
	}

	public record AssemblyPartRelationship(
		String catenaXId,
		List<ChildPart> childParts
	) {}

	public record ManufacturingInformation(
		String country,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss", timezone = "CET") Date date
	) {}

	public enum LocalIdType {
		@JsonProperty("ManufacturerID")
		MANUFACTURER_ID,
		@JsonProperty("ManufacturerPartID")
		MANUFACTURER_PART_ID,
		@JsonProperty("PartInstanceID")
		PART_INSTANCE_ID,
		@JsonEnumDefaultValue UNKNOWN
	}

	public record LocalId(
		@JsonProperty("key") LocalIdType type,
		String value
	) {}
}