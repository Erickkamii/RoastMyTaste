package dev.erick.roastmytaste.infrastructure.spotify;

import dev.erick.roastmytaste.application.port.out.SpotifyProvider;
import dev.erick.roastmytaste.domain.exception.SpotifyProviderException;
import dev.erick.roastmytaste.domain.model.Artist;
import dev.erick.roastmytaste.domain.model.Track;
import dev.erick.roastmytaste.infrastructure.spotify.dto.SpotifyArtistsResponse;
import dev.erick.roastmytaste.infrastructure.spotify.dto.SpotifyTrackResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class SpotifyProviderAdapter implements SpotifyProvider {

    private static final String DEFAULT_TIME = "long_term";
    private static final int DEFAULT_LIMIT = 20;

    private final SpotifyApiClient spotifyApiClient;

    public  SpotifyProviderAdapter(SpotifyApiClient spotifyApiClient) {
        this.spotifyApiClient = spotifyApiClient;
    }

    @Override
    public List<Track> getTopTracks(String accessToken) {
        try{
            var response = spotifyApiClient.getTopTracks(
                    bearer(accessToken),
                    DEFAULT_TIME,
                    DEFAULT_LIMIT
            );
            if (response == null || response.items() == null || response.items().isEmpty()) {
                return List.of();
            }
            return response.items()
                    .stream()
                    .map(SpotifyTrackResponse::toDomain)
                    .toList();
        } catch (HttpClientErrorException e){
            throw mapException(e);
        } catch (RestClientException e){
            throw new SpotifyProviderException("Failed to fetch top track from Spotify.",e);
        }
    }

    @Override
    public List<Artist> getTopArtists(String accessToken) {
        try{
            var response = spotifyApiClient.getTopArtists(
                    bearer(accessToken),
                    DEFAULT_TIME,
                    DEFAULT_LIMIT
            );
            if (response == null || response.items() == null || response.items().isEmpty()) {
                return List.of();
            }
            return response.items()
                    .stream()
                    .map(SpotifyArtistsResponse::toDomain)
                    .toList();
        } catch (HttpClientErrorException e){
            throw mapException(e);
        } catch (RestClientException e){
            throw new SpotifyProviderException("Failed to fetch top artists from Spotify.",e);
        }
    }

    private String bearer(String accessToken) {
        return "Bearer " + accessToken;
    }

    private SpotifyProviderException mapException(HttpClientErrorException e){
        return switch (e.getStatusCode()) {
            case HttpStatus.UNAUTHORIZED ->
                    new SpotifyProviderException("Spotify access token is invalid or expired.", e);
            case HttpStatus.FORBIDDEN ->
                    new SpotifyProviderException("Spotify access denied, check if the OAuth scope user-top-read was granted.", e);
            case HttpStatus.TOO_MANY_REQUESTS ->
                    new SpotifyProviderException("Spotify rate limit exceeded. Try again later.", e);
            case HttpStatus.NOT_FOUND -> new SpotifyProviderException("Spotify resource not found.", e);
            default -> new SpotifyProviderException("Spotify request failed with status " + e.getStatusCode(), e);
        };

    }
}
