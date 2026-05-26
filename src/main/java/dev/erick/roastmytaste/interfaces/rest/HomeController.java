package dev.erick.roastmytaste.interfaces.rest;

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
}