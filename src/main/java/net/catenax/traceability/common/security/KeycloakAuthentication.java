package net.catenax.traceability.common.security;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static java.util.Collections.emptySet;

public class KeycloakAuthentication {

	private final String userId;
	private final Set<KeycloakRole> keycloakRoles;

	public KeycloakAuthentication(String userId, Set<KeycloakRole> keycloakRoles) {
		Assert.hasText(userId, "UserId must be present");
		this.userId = userId;
		this.keycloakRoles = Collections.unmodifiableSet(keycloakRoles);
	}

	public static KeycloakAuthentication noRoles(String userId) {
		return new KeycloakAuthentication(userId, emptySet());
	}

	public boolean hasRole(KeycloakRole keycloakRole) {
		return keycloakRoles.contains(keycloakRole);
	}

	public String getUserId() {
		return userId;
	}

	public boolean hasAtLeastOneRole(KeycloakRole... keycloakRole) {
		return Arrays.stream(keycloakRole)
			.map(this::hasRole)
			.filter(hasRole -> hasRole)
			.findFirst()
			.orElse(false);
	}

	@Override
	public String toString() {
		return "KeycloakAuthentication{" +
			"userId='" + userId + '\'' +
			", keycloakRoles=" + keycloakRoles +
			'}';
	}
}
