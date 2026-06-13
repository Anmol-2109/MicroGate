package com.Anmol.Gateway.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class SecurityUtil {


    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${jwt.accessExpiration}")
    private Long accessExpiration;

    @Value("${jwt.refreshExpiration}")
    private Long refreshExpiration;


    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String getUsernameFromToken(String token){

        return extractClaims(token)
                .getSubject();
    }


    public Claims extractClaims(String token){

        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public  long getUserIdFromToken(String token) {
        Object userId =
                extractClaims(token)
                        .get("userId");

        return Long.valueOf(
                userId.toString()
        );
    }
}
