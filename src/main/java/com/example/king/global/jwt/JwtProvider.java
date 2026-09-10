package com.example.king.global.jwt;

import com.example.king.domain.auth.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey key;
    public JwtProvider(
            @Value("${spring.jwt.secret}") String secret
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + 30 * 60 * 1000); // 30분

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expirationTime)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + 7L * 24 * 60 * 60 * 1000); // 7일

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiration(expirationTime)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token){

        try{
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public Claims getClaims(String token){

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }
}