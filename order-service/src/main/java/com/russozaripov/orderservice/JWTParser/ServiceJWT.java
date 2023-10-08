package com.russozaripov.orderservice.JWTParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class ServiceJWT {

    @Value("${jwt.secret}")
    private String secret;
        public String getUserName(String token){
        Claims claims = getClaimsToken(token);
        return claims.get("username", String.class);
    }
    private Claims getClaimsToken(String token){
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }
}
