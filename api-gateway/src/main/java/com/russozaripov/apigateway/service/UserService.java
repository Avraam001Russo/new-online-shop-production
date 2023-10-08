package com.russozaripov.apigateway.service;


import com.russozaripov.apigateway.entity.UserEntity;
import com.russozaripov.apigateway.entity.UserRole;
import com.russozaripov.apigateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public Mono<UserEntity> registerUser(UserEntity user){
        log.info(user.toString());
        return userRepository.save(
                user.toBuilder()
                        .username(user.getUsername())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(savedUser -> log.info("User successfully saved: %s".formatted(savedUser)));
    }
    public Mono<UserEntity> findByUserId(Long id){
        return userRepository.findById(id);
    }
    public Mono<UserEntity> findByUserName(String username){
        return userRepository.findByUsername(username);
    }
}
