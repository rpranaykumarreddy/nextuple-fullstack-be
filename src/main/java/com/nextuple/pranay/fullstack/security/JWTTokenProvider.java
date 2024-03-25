package com.nextuple.pranay.fullstack.security;

import com.nextuple.pranay.fullstack.exception.CustomException;
import com.nextuple.pranay.fullstack.repo.UsersRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTTokenProvider {
//    @Value("${app.jwt-secret}")
    private String jwtSecret =  System.getenv("JWT_SECRET");
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;
    @Autowired
    private UsersRepo usersRepo;

    public String generateToken(Authentication authentication) {
        String userLogin = authentication.getName();
        String username = usersRepo.findByUsernameOrEmailIgnoreCase(userLogin,userLogin).orElseThrow(
                ()-> new CustomException.EntityNotFoundException("User not found with username: "+userLogin)
        ).getUsername();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder().setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
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
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String regenerateToken(String username){
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        return Jwts.builder().setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

}
