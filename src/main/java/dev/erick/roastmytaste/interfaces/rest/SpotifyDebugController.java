package dev.erick.roastmytaste.interfaces.rest;

import dev.erick.roastmytaste.application.port.out.SpotifyProvider;
import dev.erick.roastmytaste.domain.model.Artist;
import dev.erick.roastmytaste.domain.model.Track;
import dev.erick.roastmytaste.domain.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/debug")
public class SpotifyDebugController {

    private final Logger logger = LoggerFactory.getLogger(SpotifyDebugController.class);
    private final SpotifyProvider spotifyProvider;

    public SpotifyDebugController(SpotifyProvider spotifyProvider) {
        this.spotifyProvider = spotifyProvider;
    }

    @GetMapping("/top-tracks")
    public List<Track> topTracks(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "spotify_token", required = false) String cookieToken) {

        String token = extractToken(authHeader, cookieToken);
        if (token == null) {
            throw new RuntimeException("Token não encontrado");
        }

        logger.debug("Getting tracks from Spotify");
        return spotifyProvider.getTopTracks(token);
    }

    @GetMapping("/top-artists")
    public List<Artist> topArtists(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "spotify_token", required = false) String cookieToken) {

        String token = extractToken(authHeader, cookieToken);
        if (token == null) {
            throw new RuntimeException("Token não encontrado");
        }

        return spotifyProvider.getTopArtists(token);
    }

    @GetMapping("/profile")
    public UserProfile profile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "spotify_token", required = false) String cookieToken) {

        String token = extractToken(authHeader, cookieToken);
        if (token == null) {
            throw new RuntimeException("Token não encontrado");
        }

        logger.debug("Getting profile from Spotify");
        var tracks = spotifyProvider.getTopTracks(token);
        var artists = spotifyProvider.getTopArtists(token);
        return UserProfile.from(tracks, artists);
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