package com.PhonePay.Clone.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonweb token.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Token sign karne ke liye ek secure key generate kar rahe hain
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token kitni der tak valid rahega (e.g., 10 Din)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 10;

    // Login ke waqt token banane ka method
    public String generateToken(String phoneNumber) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Token se Phone Number nikalne ka method
    public String extractPhoneNumber(String token) {
        return getClaims(token).getSubject();
    }

    // Token valid hai ya expire ho gaya, check karne ka method
    public boolean isTokenValid(String token, String phoneNumber) {
        String extractedPhone = extractPhoneNumber(token);
        return (extractedPhone.equals(phoneNumber) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}