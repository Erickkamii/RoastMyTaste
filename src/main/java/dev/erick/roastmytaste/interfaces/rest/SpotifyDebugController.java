package dev.erick.roastmytaste.interfaces.rest;

import dev.erick.roastmytaste.application.port.out.SpotifyProvider;
import dev.erick.roastmytaste.domain.model.Artist;
import dev.erick.roastmytaste.domain.model.Track;
import dev.erick.roastmytaste.domain.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<Track> topTracks(@AuthenticationPrincipal Jwt jwt) {
        String spotifyToken = jwt.getClaimAsString("spotifyAccessToken");
        logger.debug("Getting tracks from Spotify");
        return spotifyProvider.getTopTracks(spotifyToken);
    }

    @GetMapping("/top-artists")
    public List<Artist> topArtists(@AuthenticationPrincipal Jwt jwt) {
        String spotifyToken = jwt.getClaimAsString("spotifyAccessToken");
        logger.debug("Getting artists from Spotify");
        return spotifyProvider.getTopArtists(spotifyToken);
    }

    @GetMapping("/profile")
    public UserProfile profile(@AuthenticationPrincipal Jwt jwt) {
        String spotifyToken = jwt.getClaimAsString("spotifyAccessToken");
        logger.debug("Getting profile from Spotify");

        var tracks = spotifyProvider.getTopTracks(spotifyToken);
        var artists = spotifyProvider.getTopArtists(spotifyToken);
        return UserProfile.from(tracks, artists);
    }
}