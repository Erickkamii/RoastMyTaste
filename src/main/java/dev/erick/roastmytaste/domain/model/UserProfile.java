package dev.erick.roastmytaste.domain.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public record UserProfile(
        List<Track> topTracks,
        List<Artist> topArtists,
        double averageTrackDuration,
        List<String> dominantGenres
) {
    public UserProfile{
        topTracks = List.copyOf(Objects.requireNonNull(topTracks));
        topArtists = List.copyOf(Objects.requireNonNull(topArtists));
        dominantGenres = List.copyOf(Objects.requireNonNull(dominantGenres));
    }

    public static UserProfile from(List<Track> tracks, List<Artist> artists) {
        var topTracks = List.copyOf(tracks);
        var topArtists = List.copyOf(artists);

        double averageTrackPopularity = topTracks.stream()
                .mapToInt(Track::duration)
                .average()
                .orElse(0.0);

        List<String> dominantGenres = topArtists.stream()
                .flatMap(a -> a.genres().stream())
                .collect(Collectors.groupingBy(
                        g -> g,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(8)
                .map(Map.Entry::getKey)
                .toList();

        return new UserProfile(
                topTracks,
                topArtists,
                averageTrackPopularity,
                dominantGenres
        );
    }
}
