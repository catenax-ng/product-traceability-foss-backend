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



package org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.query;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * This class can be used to form select expressions e.g. in SQL statements. It is a way to express
 * those statements in a generic way.
 * For example:
 * <pre>
 * "operandLeft" = "name",
 * "operator" = "=",
 * "operandRight" = "someone"
 * </pre>
 * <p>
 * can be translated to {@code [select * where name = someone]}
 */
public class Criterion {
	@JsonProperty("left")
	private Object operandLeft;
	@JsonProperty("op")
	private String operator;
	@JsonProperty("right")
	private Object operandRight;

	private Criterion() {
		//for json serialization
	}

	public Criterion(Object left, String op, Object right) {
		operandLeft = Objects.requireNonNull(left);
		operator = Objects.requireNonNull(op);
		operandRight = right; // null may be allowed, for example when the operator is unary, like NOT_NULL
	}

	public Object getOperandLeft() {
		return operandLeft;
	}

	public String getOperator() {
		return operator;
	}

	public Object getOperandRight() {
		return operandRight;
	}

	public Criterion withLeftOperand(Function<Object, Object> function) {
		return new Criterion(function.apply(this.operandLeft), this.operator, this.getOperandRight());
	}

	@Override
	public int hashCode() {
		return Objects.hash(operandLeft, operator, operandRight);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Criterion criterion = (Criterion) o;
		return Objects.equals(operandLeft, criterion.operandLeft) && Objects.equals(operator, criterion.operator) && Objects.equals(operandRight, criterion.operandRight);
	}

	@Override
	public String toString() {
		return format("%s %s %s", getOperandLeft(), getOperator(), getOperandRight());
	}
}
