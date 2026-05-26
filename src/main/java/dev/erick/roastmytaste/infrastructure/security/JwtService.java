package dev.erick.roastmytaste.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(OAuth2User oAuth2User, String spotifyAccessToken) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("roast-my-taste")
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationMs))
                .subject(oAuth2User.getName())
                .claim("spotifyId", oAuth2User.getName())
                .claim("name", oAuth2User.getAttribute("display_name"))
                .claim("email", oAuth2User.getAttribute("email"))
                .claim("spotifyAccessToken", spotifyAccessToken)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}