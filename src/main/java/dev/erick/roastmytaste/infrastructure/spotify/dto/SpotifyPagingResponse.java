package dev.erick.roastmytaste.infrastructure.spotify.dto;

import java.util.List;

public record SpotifyPagingResponse<T>(
        List<T> items,
        int total,
        int limit,
        int offset
) {
}
