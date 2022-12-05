/********************************************************************************
 * Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 * Copyright (c) 2022, 2023 ZF Friedrichshafen AG
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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


package org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * A collection of child constraints where all must be satisfied for the constraint to be satisfied.
 */
@JsonDeserialize(builder = AndConstraint.Builder.class)
public class AndConstraint extends MultiplicityConstraint {

	private AndConstraint() {
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitAndConstraint(this);
	}

	@Override
	public AndConstraint create(List<Constraint> constraints) {
		return Builder.newInstance().constraints(constraints).build();
	}

	@Override
	public String toString() {
		return "And constraint: [" + constraints.stream().map(Object::toString).collect(joining(",")) + "]";
	}

	@JsonPOJOBuilder(withPrefix = "")
	public static class Builder extends MultiplicityConstraint.Builder<AndConstraint, Builder> {

		private Builder() {
			constraint = new AndConstraint();
		}

		@JsonCreator
		public static Builder newInstance() {
			return new Builder();
		}

		public AndConstraint build() {
			return constraint;
		}
	}
}
