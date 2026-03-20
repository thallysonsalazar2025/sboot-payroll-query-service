package com.thallyson.sboot.payrollqueryservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtTokenExtractor {

    private static final String SECRET_KEY = "your-secret-key"; // Use a secure key in production

    public static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
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