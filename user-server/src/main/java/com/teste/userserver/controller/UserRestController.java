package com.teste.userserver.controller;

import static com.teste.userserver.constants.ErrorMessage.MISSED_AUTHORIZATION_HEADER;
import static com.teste.userserver.constants.ErrorMessage.USER_NOT_FOUND;
import static com.teste.userserver.constants.PathConstants.API_V1_USER;
import static com.teste.userserver.constants.PathConstants.MY_ACCOUNT;
import static com.teste.userserver.constants.PathConstants.USER_ID;

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.teste.userserver.dto.AccountDto;
import com.teste.userserver.dto.UserDto;
import com.teste.userserver.dto.security.CustomPrincipal;
import com.teste.userserver.exception.InvalidCredentialsException;
import com.teste.userserver.exception.NotFoundException;
import com.teste.userserver.mapper.UserMapper;
import com.teste.userserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_USER)
public class UserRestController {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final ReactiveCircuitBreakerFactory cbFactory;

    private final WebClient.Builder webClientBuilder;

    @GetMapping(MY_ACCOUNT)
    public Mono<AccountDto> handleGetAccountInfo(Authentication authentication) {
        if (authentication != null) {
            var customPrincipal = (CustomPrincipal) authentication.getPrincipal();
            var userMono = userRepository.findById(customPrincipal.getId());

            return userMono.map(userMapper::mapUserToAccountDto)
                    .switchIfEmpty(Mono.error(new NotFoundException(USER_NOT_FOUND + customPrincipal.getId())));
        } else {
            return Mono.error(new InvalidCredentialsException(MISSED_AUTHORIZATION_HEADER));
        }
    }

    @GetMapping(USER_ID)
    public Mono<UserDto> handleGetUserById(@PathVariable String id) {
        var userMono = userRepository.findById(id);

        return userMono.map(userMapper::mapUserToUserDto)
                .switchIfEmpty(Mono.error(new NotFoundException(USER_NOT_FOUND + id)));
    }
}