package dev.erick.roastmytaste.interfaces.rest;

import dev.erick.roastmytaste.application.port.out.SpotifyProvider;
import dev.erick.roastmytaste.domain.model.Artist;
import dev.erick.roastmytaste.domain.model.Track;
import dev.erick.roastmytaste.domain.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
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
    public List<Track> topTracks(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient){
        logger.debug("Getting tracks from Spotify");
        return spotifyProvider.getTopTracks(authorizedClient.getAccessToken().getTokenValue());
    }

    @GetMapping("/top-artists")
    public List<Artist> topArtists(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient){
        logger.debug("Getting artists from Spotify");
        return spotifyProvider.getTopArtists(authorizedClient.getAccessToken().getTokenValue());
    }

    @GetMapping("/profile")
    public UserProfile profile(@RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient){
        logger.debug("Getting profile from Spotify");
        var tracks = spotifyProvider.getTopTracks(authorizedClient.getAccessToken().getTokenValue());
        var artists = spotifyProvider.getTopArtists(authorizedClient.getAccessToken().getTokenValue());
        return UserProfile.from(tracks, artists);
    }
}