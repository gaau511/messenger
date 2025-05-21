package dev.gaau.messenger.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.time.Instant;
import java.util.Optional;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret-key}") String key) {
        byte[] decodedKey = Decoders.BASE64.decode(key);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public Optional<Claims> resolveToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
            return Optional.of(claims);
        } catch (IllegalArgumentException | JwtException e) {
            return Optional.empty();
        }
    }

    public Boolean isValidToken(String token) {
        if (resolveToken(token).isEmpty())
            return false;

        return true;
    }

    public Optional<String> parseAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null)
            return Optional.empty();

        try {
            return Optional.of(authorizationHeader.substring(7));
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Cannot parse Authorization header.");
        }
    }
}
