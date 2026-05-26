package dev.erick.roastmytaste.interfaces.rest;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
public class AuthDebugController {

    private final RestClient spotifyRestClient = RestClient.create("https://api.spotify.com/v1");

    @GetMapping("/api/v1/me")
    public Map<String, Object> me(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "spotify_token", required = false) String cookieToken) {

        String token = extractToken(authHeader, cookieToken);

        if (token == null) {
            return Map.of("authenticated", false);
        }

        try {
            Map<String, Object> spotifyUser = spotifyRestClient.get()
                    .uri("/me")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(Map.class);

            if (spotifyUser == null) {
                return Map.of("authenticated", false);
            }

            return Map.of(
                    "authenticated", true,
                    "name", spotifyUser.get("id"),
                    "authorities", Map.of("attributes", spotifyUser)
            );

        } catch (Exception e) {
            return Map.of("authenticated", false);
        }
    }
    private String extractToken(String authHeader, String cookieToken) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        if (cookieToken != null && !cookieToken.isBlank()) {
            return cookieToken;
        }
        return null;
    }

}