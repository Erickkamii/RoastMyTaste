package dev.erick.roastmytaste.interfaces.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthDebugController {

    @GetMapping("/api/v1/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) {
            return Map.of("authenticated", false);
        }

        return Map.of(
                "authenticated", true,
                "spotifyId", principal.getName(),
                "displayName", principal.getAttribute("display_name"),
                "email", principal.getAttribute("email"),
                "images", principal.getAttribute("images"),
                "authorities", principal.getAuthorities()
        );
    }

    @GetMapping("/api/v1/auth/status")
    public Map<String, Object> authStatus(
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient client) {

        return Map.of(
                "authenticated", true,
                "clientRegistrationId", client.getClientRegistration().getRegistrationId(),
                "tokenType", client.getAccessToken().getTokenType().getValue(),
                "tokenExpiresAt", client.getAccessToken().getExpiresAt(),
                "scopes", client.getAccessToken().getScopes()
        );
    }
}