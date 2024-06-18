package com.teste.userserver.controller;

import lombok.RequiredArgsConstructor;

import static com.teste.userserver.constants.PathConstants.API_V1_AUTH;
import static com.teste.userserver.constants.PathConstants.SIGN_IN;
import static com.teste.userserver.constants.PathConstants.SIGN_UP;
import static com.teste.userserver.constants.PathConstants.TOKEN_PARAM;
import static com.teste.userserver.constants.PathConstants.VALIDATE;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teste.userserver.dto.security.AuthRequestDto;
import com.teste.userserver.dto.security.AuthResponseDto;
import com.teste.userserver.dto.security.RegistrationRequest;
import com.teste.userserver.dto.security.RegistrationResponse;
import com.teste.userserver.dto.security.UserValidationResponse;
import com.teste.userserver.mapper.UserMapper;
import com.teste.userserver.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_AUTH)
public class AuthRestController {

    private final AuthService authService;

    private final UserMapper userMapper;

    @PostMapping(SIGN_UP)
    public Mono<RegistrationResponse> handleSignUp(@RequestBody RegistrationRequest registrationRequest) {
        return authService.registerUser(userMapper.mapRegistrationRequestToUser(registrationRequest))
                .map(userMapper::mapUserToRegistrationResponse);
    }

    @Operation(summary = "Get all products", description = "Read all products from the database")
    @ApiResponse(responseCode = "200", description = "Return all products")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping(SIGN_IN)
    public Mono<AuthResponseDto> handleSignIn(@RequestBody AuthRequestDto dto) {
        return authService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()));
    }

    @PostMapping(VALIDATE)
    public Mono<UserValidationResponse> handleValidateToken(@RequestParam(TOKEN_PARAM) String token) {
        return authService.validateToken(token);
    }
}
