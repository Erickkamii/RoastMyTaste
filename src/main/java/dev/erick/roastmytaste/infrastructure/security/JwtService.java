package dev.erick.roastmytaste.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(OAuth2User oAuth2User, String spotifyAccessToken) {
        Instant now = Instant.now();

        Map<String, Object> claims = new HashMap<>();
        claims.put("spotifyId", oAuth2User.getName());
        claims.put("name", oAuth2User.getAttribute("display_name"));
        claims.put("email", oAuth2User.getAttribute("email"));
        claims.put("spotifyAccessToken", spotifyAccessToken);

        return Jwts.builder()
                .claims(claims)
                .subject(oAuth2User.getName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(getSigningKey())
                .compact();
    }
}