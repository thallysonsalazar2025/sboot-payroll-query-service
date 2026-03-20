package com.thallyson.sboot.payrollqueryservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

public class JwtTokenExtractor {

    // NOTE: replace with a secure value from configuration
    private static final String SECRET_KEY = "your-secret-key-should-be-at-least-32-chars!";
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public static Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public static Date getExpirationDate(String token) {
        return extractClaims(token).getExpiration();
    }

    public static Boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }
}
