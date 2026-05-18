# SpotifyRoaster

Descrição
---------
O SpotifyRoaster é uma API backend (sem frontend) que autentica usuários via OAuth do Spotify e gera "roasts" sarcásticos sobre o gosto musical do usuário usando um provedor de IA. O projeto foi concebido como um backend de portfólio, mostrando integração segura com OAuth, consumo de APIs externas e uma arquitetura simples e de fácil manutenção.

Funcionalidades principais
--------------------------
- Login via Spotify (OAuth2 Authorization Code Flow)
- Gerenciamento automático de tokens pelo Spring Security
- Coleta dos top tracks e top artists do usuário
- Geração de análise / roast por IA
- Endpoints de debug para desenvolvimento
- API-only (sem frontend)

Visão geral da arquitetura
--------------------------
O projeto segue uma organização com separação de responsabilidades (inspirada em Clean Architecture / hexagonal):

- Camada de aplicação: `AnalyzeMusicTasteService` orquestra a coleta dos dados do Spotify e a chamada ao provedor de IA.
- Domínio: modelos (`RoastAnalysis`, `UserProfile`, `Track`, `Artist`) e exceções específicas (`InsufficientDataException`, `AiAnalysisException`, `SpotifyProviderException`).
- Infraestrutura: `SpotifyProviderAdapter` + `SpotifyApiClient` para comunicação com a API do Spotify; `SpringAiAnalyzerProvider` e `FakeAiAnalyzerProvider` para integração com IA.
- Interface HTTP: controllers REST em `interfaces/rest` (ex.: `SpotifyAnalyzisDebugController`, `SpotifyDebugController`, `HomeController`).

Tecnologias
-----------
- Java 25
- Spring Boot 4
- Spring Security OAuth2 Client
- Spring AI (ChatClient)
- Gradle

Fluxo de autenticação (resumo)
-----------------------------
1. Cliente acessa: GET /oauth2/authorization/spotify
2. Usuário autentica no Spotify e concede permissões
3. Spotify redireciona para `{baseUrl}/login/oauth2/code/spotify`
4. Spring Security troca o código por um access token e gerencia o ciclo de vida do token
5. Controllers usam `OAuth2AuthorizedClient` para obter o token e chamar Spotify/IA

Configuração / Variáveis de ambiente
-----------------------------------
Crie um app no Spotify Developer Dashboard e configure o Redirect URI. Em seguida defina as variáveis de ambiente:

    `SPOTIFY_CLIENT_ID` — client id do app Spotify
    `SPOTIFY_SECRET` — client secret do app Spotify
    `GROQ_API_URL` — URL base do provedor de IA (ex: OpenAI API base url)
    `GROQ_API_KEY` — chave da API do provedor de IA (usada em `application.yml` como `spring.ai.openai.api-key`)

Exemplo de Redirect URI para desenvolvimento:

```
http://127.0.0.1:8080/login/oauth2/code/spotify
```

Como rodar (local)
------------------
Rodar a aplicação:

```bash
./gradlew bootRun
```

Rodar com perfil `local` (usa `FakeAiAnalyzerProvider`):

```bash
./gradlew bootRun -Dspring.profiles.active=local
# ou
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

Endpoints úteis
---------------
- `GET /oauth2/authorization/spotify` — inicia o login no Spotify
- `GET /api/v1/analysis` — gera a análise/roast (debug) com o token do usuário autenticado
- `GET /api/v1/debug/top-tracks` — retorna os top tracks do usuário
- `GET /api/v1/debug/top-artists` — retorna os top artists do usuário
- `GET /api/v1/debug/profile` — retorna o `UserProfile` construído a partir dos dados do Spotify
- `GET /` — endpoint simples que indica que a API está rodando
- `GET /id` — endpoint de debug que imprime o client id carregado

Formato da resposta de análise
-----------------------------
A análise retornada pela IA é mapeada para o `record` Java `RoastAnalysis` com os campos:

- `title`
- `musicalDiagnosis`
- `roast`
- `recommendation`
- `chaosScore` (0 a 100)
- `personalityTags` (lista de strings)

Perfis de execução e provedores de IA
------------------------------------
- `FakeAiAnalyzerProvider` é ativado quando o perfil `local` está ativo e fornece uma resposta fixa para desenvolvimento.
- `SpringAiAnalyzerProvider` é ativado por padrão (perfil diferente de `local`) e usa o `ChatClient` para conversar com um modelo de IA.
