package dev.erick.roastmytaste.domain.model;


import java.util.List;
import java.util.Objects;

public record Artist(
        String id,
        String name,
        List<String> genres
) {
    public Artist{
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");

        genres = genres == null ? List.of() : List.copyOf(genres);
    }
}
