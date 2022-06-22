package net.catenax.traceability.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.util.Optional;

public class KeycloakAuthorizationInterceptor implements RequestInterceptor {

	private final Logger logger = LoggerFactory.getLogger(KeycloakAuthorizationInterceptor.class);

	private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

	public KeycloakAuthorizationInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
		this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
	}

	@Override
	public void apply(RequestTemplate template) {
		if (!template.headers().containsKey(HttpHeaders.AUTHORIZATION)) {
			OAuth2AccessToken accessToken = getAccessToken().orElseThrow(() -> new RuntimeException("TODO"));

			logger.debug("Injecting access token value {} of type {}", accessToken.getTokenValue(), accessToken.getTokenType());

			template.header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(accessToken.getTokenValue()));
		}
	}

	private Optional<OAuth2AccessToken> getAccessToken() {
		OAuth2AuthorizeRequest keycloak = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
			.principal(SecurityContextHolder.getContext()
				.getAuthentication())
			.build();

		OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientManager.authorize(keycloak);

		return Optional.ofNullable(oAuth2AuthorizedClient)
			.map(OAuth2AuthorizedClient::getAccessToken);
	}
}
