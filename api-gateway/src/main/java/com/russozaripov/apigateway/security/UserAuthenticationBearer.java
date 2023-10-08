package com.russozaripov.apigateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

public class UserAuthenticationBearer {
    public static Mono<Authentication> create(JwtHandler.VerificationResult verificationResult){
        Claims claims = verificationResult.claims; // достаем payload
        String subject = claims.getSubject();

        String role = claims.get("role", String.class);
        String username = claims.get("username", String.class); // достаем из payload то что нам нужно

        List<SimpleGrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority(role)); // переводим роль в тип данных спринговой роли
        Long principalID = Long.parseLong(subject);
        CustomPrincipal principal = new CustomPrincipal(principalID, username);

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorityList));
    }
}
