package dev.erick.roastmytaste.interfaces.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return """
				Spotify Roaster API is running

				Login:
				GET /oauth2/authorization/spotify

				Debug user:
				GET /api/v1/me
				""";
    }

	@Value("${spring.security.oauth2.client.registration.spotify.client-id}")
	private String clientId;

	@GetMapping("/id")
	public String home2() {
		System.out.println("DEBUG - Client ID carregado: " + clientId);
		return "Running...";
	}
}
