package com.russozaripov.apigateway.config;

import com.russozaripov.apigateway.security.AuthenticationManager;
import com.russozaripov.apigateway.security.BearerTokenServerAuthenticationConverter;
import com.russozaripov.apigateway.security.JwtHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
@ComponentScan(basePackages = "com.russozaripov")
@Slf4j
public class WebSecurityConfig {

    @Value("${jwt.secret}")
    private String secret;
    private final String[] authRoutes = {"/api/v1/auth/registration", "/api/v1/auth/login"};
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity, AuthenticationManager authenticationManager){
        return httpSecurity
                .csrf(csrfSpec -> csrfSpec.disable())
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .pathMatchers(HttpMethod.OPTIONS)
                                .permitAll()
                                .pathMatchers(authRoutes)
                                .permitAll()
                                .anyExchange().authenticated())
                .exceptionHandling(exceptionHandlingSpec ->
                        exceptionHandlingSpec.authenticationEntryPoint((swe, e) -> {
                            log.info("Error while securityWebFilterChain -> HttpStatus.UNAUTHORIZED: {}", e.getMessage());
                            return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                        })

                ).exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.accessDeniedHandler((swe, e) -> {
                    log.info("Error while securityWebFilterChain -> HttpStatus.FORBIDDEN: {}", e.getMessage());
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                }))
                .addFilterAt(bearerAuthenticationFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
    public AuthenticationWebFilter bearerAuthenticationFilter (AuthenticationManager authenticationManager){
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**")); // вообще все запросы проходят через данный фильтр
        return bearerAuthenticationFilter;
    }

}
