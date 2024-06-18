package com.teste.userserver.service;

import com.teste.userserver.dto.security.UserValidationResponse;
import com.teste.userserver.model.User;
import com.teste.userserver.vo.TokenDetailsVo;

import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<TokenDetailsVo> authenticate(String username, String password);

    Mono<User> registerUser(User user);

    Mono<UserValidationResponse> validateToken(String token);
}
