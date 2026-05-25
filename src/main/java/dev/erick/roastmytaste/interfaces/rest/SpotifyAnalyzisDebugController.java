package dev.erick.roastmytaste.interfaces.rest;

import dev.erick.roastmytaste.application.port.in.AnalyzeMusicTasteUseCase;
import dev.erick.roastmytaste.domain.model.RoastAnalysis;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyAnalyzisDebugController {

    private final AnalyzeMusicTasteUseCase analyzeMusicTasteUseCase;

    public SpotifyAnalyzisDebugController(AnalyzeMusicTasteUseCase analyzeMusicTasteUseCase) {
        this.analyzeMusicTasteUseCase = analyzeMusicTasteUseCase;
    }

    @GetMapping("/api/v1/analysis")
    public RoastAnalysis analyze(@AuthenticationPrincipal Jwt jwt) {
        String spotifyToken = jwt.getClaimAsString("spotifyAccessToken");
        return analyzeMusicTasteUseCase.analyze(spotifyToken);
    }
}