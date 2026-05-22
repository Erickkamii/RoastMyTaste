package dev.erick.roastmytaste.application.port.out;

import dev.erick.roastmytaste.domain.model.Artist;
import dev.erick.roastmytaste.domain.model.Track;

import java.util.List;

public interface SpotifyProvider {
    List<Track> getTopTracks(String accessToken);

    List<Artist> getTopArtists(String accessToken);
}
