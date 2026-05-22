package dev.erick.roastmytaste.infrastructure.spotify;

import dev.erick.roastmytaste.infrastructure.spotify.dto.SpotifyArtistsResponse;
import dev.erick.roastmytaste.infrastructure.spotify.dto.SpotifyPagingResponse;
import dev.erick.roastmytaste.infrastructure.spotify.dto.SpotifyTrackResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/me/top")
public interface SpotifyApiClient {

    @GetExchange("/tracks")
    SpotifyPagingResponse<SpotifyTrackResponse> getTopTracks(
            @RequestHeader("Authorization") String token,
            @RequestParam("time_range") String timeRange,
            @RequestParam("limit") int limit
    );

    @GetExchange("/artists")
    SpotifyPagingResponse<SpotifyArtistsResponse> getTopArtists(
            @RequestHeader("Authorization") String token,
            @RequestParam("time_range") String timeRange,
            @RequestParam("limit") int limit
    );
}
