package com.nextuple.pranay.fullstack.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component
public class JWTTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private String jwtExpirationDate;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        String token = Jwts.builder().setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }
    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
    public String getUsername(String token){
        Claims claims=Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJwt(token).getBody();
        String username= claims.getSubject();
        return username;
    }

    public boolean valiateToken(String token){
        Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(token);
        return true;

    }
}
