package com.russozaripov.apigateway.REST;


import com.russozaripov.apigateway.dto.AuthRequestDTO;
import com.russozaripov.apigateway.dto.AuthResponseDTO;
import com.russozaripov.apigateway.dto.UserDTO;
import com.russozaripov.apigateway.entity.UserEntity;
import com.russozaripov.apigateway.security.CustomPrincipal;
import com.russozaripov.apigateway.security.SecurityService;
import com.russozaripov.apigateway.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class RestControllerV1 {
    private final UserService userService;
    private final SecurityService securityService;
//    private final UserMapper userMapper;

    public RestControllerV1(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
//        this.userMapper = userMapper;
    }

    @PostMapping("/registration")
    public Mono<UserDTO> register(@RequestBody UserDTO userDTO){
        log.info(userDTO.toString());
        UserEntity userEntity = UserEntity.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .lastName(userDTO.getLastName())
                .firstName(userDTO.getFirstName())
                .enabled(userDTO.isEnabled())
                .role(userDTO.getRole())
                .updatedAt(userDTO.getUpdatedAt())
                .createdAt(userDTO.getCreatedAt())
                .id(userDTO.getId())
                .build();
        return userService.registerUser(userEntity)
                .map(user -> UserDTO.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .role(user.getRole())
                        .updatedAt(user.getUpdatedAt())
                        .createdAt(user.getCreatedAt())
                        .id(user.getId())
                        .enabled(user.isEnabled())
                        .build());
    }
    @PostMapping("/login")
    public Mono<AuthResponseDTO> login(@RequestBody AuthRequestDTO dto){
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDTO.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));

    }
    @GetMapping("/info")
    public Mono<UserDTO> getInfo(Authentication authentication){
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return userService.findByUserId(customPrincipal.getId())
                .map(user -> UserDTO.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .role(user.getRole())
                        .updatedAt(user.getUpdatedAt())
                        .createdAt(user.getCreatedAt())
                        .id(user.getId())
                        .enabled(user.isEnabled())
                        .build());
    }

}
