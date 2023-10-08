package com.russozaripov.apigateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

// данный класс конвертирует запрос с вебсервера в authenticate
@RequiredArgsConstructor
public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {
    private final JwtHandler jwtHandler;
    private final static String BEARER_PREFIX = "Bearer ";

    private final static Function<String, Mono<String>> getBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(BEARER_PREFIX.length()));
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return extractHeader(exchange)
                .flatMap(getBearerValue) // вытянули сам токен
                .flatMap(token -> jwtHandler.check(token))// проверяем токен на валидность
                .flatMap(token -> UserAuthenticationBearer.create(token));// создаем Authenticate

    }

    public Mono<String> extractHeader(ServerWebExchange exchange){
        return Mono.justOrEmpty(
                exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION) // вытягиваем заголовок AUTHORIZATION
        );
    }
}
