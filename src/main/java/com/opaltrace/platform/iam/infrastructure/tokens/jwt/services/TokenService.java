package com.opaltrace.platform.iam.infrastructure.tokens.jwt.services;

import com.opaltrace.platform.iam.domain.model.aggregates.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class TokenService {

    @Value("${authorization.jwt.secret:opaltracedefaultsecretkey1234567890123456}")
    private String secret;

    @Value("${authorization.jwt.expiration.days:7}")
    private int expirationDays;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        var now = new Date();
        var expiration = new Date(now.getTime() + (long) expirationDays * 24 * 60 * 60 * 1000);

        return Jwts.builder()
                .subject(user.getEmail().value())
                .claims(Map.of(
                        "userId", user.getId(),
                        "segment", user.getSegment().name(),
                        "role", user.getRole().name(),
                        "planTier", user.getPlanTier().name()
                ))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extractPlanTier(String token) {
        return getClaims(token).get("planTier", String.class);
    }

    public String extractSegment(String token) {
        return getClaims(token).get("segment", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
