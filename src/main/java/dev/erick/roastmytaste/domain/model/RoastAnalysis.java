package dev.erick.roastmytaste.domain.model;

import java.util.List;
import java.util.Objects;

public record RoastAnalysis(
        String title,
        String musicalDiagnosis,
        String roast,
        String recommendation,
        int chaosScore,
        List<String> personalityTags
) {
    public RoastAnalysis{
        Objects.requireNonNull(title, "Title cannot be null");
        Objects.requireNonNull(musicalDiagnosis, "MusicalDiagnosis cannot be null");
        Objects.requireNonNull(roast, "Roast cannot be null");
        Objects.requireNonNull(recommendation, "Recommendation cannot be null");

        personalityTags = personalityTags == null ? List.of() : List.copyOf(personalityTags);

        if (chaosScore < 0 || chaosScore > 100) {
            throw new IllegalArgumentException("Chaos score must be between 0 and 100");
        }
    }
}
