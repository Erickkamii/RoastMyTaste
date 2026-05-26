package dev.erick.roastmytaste.interfaces.rest;

import dev.erick.roastmytaste.application.port.in.AnalyzeMusicTasteUseCase;
import dev.erick.roastmytaste.domain.model.RoastAnalysis;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyAnalyzisDebugController {

    private final AnalyzeMusicTasteUseCase analyzeMusicTasteUseCase;

    public SpotifyAnalyzisDebugController(AnalyzeMusicTasteUseCase analyzeMusicTasteUseCase) {
        this.analyzeMusicTasteUseCase = analyzeMusicTasteUseCase;
    }

    @GetMapping("/api/v1/analysis")
    public RoastAnalysis analyze(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "spotify_token", required = false) String cookieToken) {

        String token = extractToken(authHeader, cookieToken);
        if (token == null) {
            throw new RuntimeException("Token inválido ou não encontrado");
        }

        return analyzeMusicTasteUseCase.analyze(token);
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