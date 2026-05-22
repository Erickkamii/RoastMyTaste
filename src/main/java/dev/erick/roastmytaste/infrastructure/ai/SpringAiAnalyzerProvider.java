package dev.erick.roastmytaste.infrastructure.ai;

import dev.erick.roastmytaste.application.port.out.AiAnalyzisProvider;
import dev.erick.roastmytaste.domain.exception.AiAnalysisException;
import dev.erick.roastmytaste.domain.model.Artist;
import dev.erick.roastmytaste.domain.model.RoastAnalysis;
import dev.erick.roastmytaste.domain.model.Track;
import dev.erick.roastmytaste.domain.model.UserProfile;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Profile("!local")
public class SpringAiAnalyzerProvider implements AiAnalyzisProvider {
    private final ChatClient chatClient;
    public SpringAiAnalyzerProvider(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    @Override
    public RoastAnalysis analyze(UserProfile profile) {
        try{
            return chatClient.prompt()
                    .system("""
                            You are Spotify Roaster, a professional sarcastic music critic.
                            
                            Rules:
                            1 - Be funny, sarcastic and musically snobbish.
                            2 - Attack the genres, the repetitive beats, and the questionable life choices that lead to listening to these tracks.
                            3 - DO NOT use actual hate speech, racism, or slurs (keep it safe for the API filters, but make it sting).
                            4 - The diagnosis must be a satirical psychological evaluation based on their music.
                            5 - Return a structured response matching the target Java record.
                            """)
                    .user(buildPrompt(profile))
                    .call()
                    .entity(RoastAnalysis.class);
        } catch (Exception e){
            throw new AiAnalysisException("Failed to generate roast analysis with AI provider", e);
        }
    }

    public String buildPrompt(UserProfile profile) {
        return """
                Analyze the musical taste for this Spotify user based on their top data.
                
                Average Track Duration: %.2f ms

                Top Tracks:
                %s

                Top Artists:
                %s

                Generate:
                - title
                - musicalDiagnosis
                - roast
                - recommendation
                - chaosScore (from 0 to 100)
                - personalityTags
                """.formatted(
                profile.averageTrackDuration(),
                formatTracks(profile),
                formatArtists(profile)
        );
    }

    private String formatTracks(UserProfile profile) {
        return profile.topTracks()
                .stream().limit(20)
                .map(this::formatTrack)
                .collect(Collectors.joining("\n"));
    }

    private String formatTrack(Track track) {
        return "- %s by %s | duration: %d ms | explicit: %s".formatted(
                track.name(),
                String.join(", ",track.artistsNames()),
                track.duration(),
                track.explicit()
        );
    }

    private String formatArtists(UserProfile profile) {
        return profile.topArtists()
                .stream().limit(20)
                .map(this::formatArtist)
                .collect(Collectors.joining("\n"));
    }
    private String formatArtist(Artist artist) {
        return "- %s".formatted(artist.name());
    }

}
