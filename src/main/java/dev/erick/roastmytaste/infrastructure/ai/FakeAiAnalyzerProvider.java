package dev.erick.roastmytaste.infrastructure.ai;

import dev.erick.roastmytaste.application.port.out.AiAnalyzisProvider;
import dev.erick.roastmytaste.domain.model.RoastAnalysis;
import dev.erick.roastmytaste.domain.model.UserProfile;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("local")
public class FakeAiAnalyzerProvider implements AiAnalyzisProvider {

    @Override
    public RoastAnalysis analyze(UserProfile profile) {
        return new RoastAnalysis(
                "Roast Mão de Vaca.",
                "Diagnóstico musical baseado em dados reais do SEU Spotify.",
                "Seu gosto musical já foi analisado, mas a IA ainda está em modo pão-duro.",
                "Sai da sua bolha pô.",
                50,
                List.of("local", "spotify-powered")
        );
    }
}
