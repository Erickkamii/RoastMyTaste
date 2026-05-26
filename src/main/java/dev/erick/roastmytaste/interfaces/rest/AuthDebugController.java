package dev.erick.roastmytaste.interfaces.rest;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthDebugController {
    @GetMapping("/api/v1/me")
    public Map<String, Object> me(OAuth2AuthenticationToken token) {
        return Map.of(
                "name", token.getName(),
                "authorities", token.getAuthorities()
        );
    }

    @GetMapping("/api/v1/auth/status")
    public Map<String, Object> authStatus(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient client) {
        return Map.of(
                "authenticated", true,
                "clientRegistrationId", client.getClientRegistration().getRegistrationId(),
                "tokenType", client.getAccessToken().getTokenType().getValue(),
                "tokenExpiresAt", client.getAccessToken().getExpiresAt()
        );
    }
}