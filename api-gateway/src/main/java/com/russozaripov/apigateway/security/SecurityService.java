package com.russozaripov.apigateway.security;

import com.russozaripov.apigateway.entity.UserEntity;
import com.russozaripov.apigateway.exception.AuthException;
import com.russozaripov.apigateway.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SecurityService {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    private TokenDetails generateToken(UserEntity user){
        Map<String, Object> claims = new HashMap<>(){{
            put("role", user.getRole());
            put("username", user.getUsername());
        }};
        return generateToken(claims, String.valueOf(user.getId()));
    }
    private TokenDetails generateToken(Map<String, Object> claims, String subject){
        Long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);
        return generateToken(expirationDate, claims, subject);
    }

    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject){
            Date createdDate = new Date();
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuer(issuer)
                    .setSubject(subject)
                    .setIssuedAt(createdDate)
                    .setId(UUID.randomUUID().toString())
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(jwtSecret.getBytes()))
                    .compact();
            return TokenDetails.builder()
                    .token(token)
                    .issuedAt(createdDate)
                    .expiresAt(expirationDate)
                    .build();
    }


    public Mono<TokenDetails> authenticate(String username, String password){ // получаем пользователя из базы аднных
        return userService.findByUserName(username)
                .flatMap(userEntity -> {
                    if(!userEntity.isEnabled()){
                    return Mono.error(new AuthException("Account disabled", "USER_ACCOUNT_DISABLED_EXCEPTION"));
                    }
                    if (!passwordEncoder.matches(password, userEntity.getPassword())){ // проверяем пароли
                        return Mono.error(new AuthException("Invalid password", "INVALID_PASSWORD_EXCEPTION"));
                    }
                    return Mono.just(generateToken(userEntity).toBuilder().userId(userEntity.getId()).build()); // генерируем токен
                })
                .switchIfEmpty(Mono.error(new AuthException("Invalid username", "INVALID_USERNAME_EXCEPTION")));
    }
}
