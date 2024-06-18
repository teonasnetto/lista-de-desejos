package com.teste.userserver.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.teste.userserver.dto.security.UserValidationResponse;
import com.teste.userserver.exception.InvalidCredentialsException;
import com.teste.userserver.model.User;
import com.teste.userserver.model.UserRole;
import com.teste.userserver.repository.UserRepository;
import com.teste.userserver.vo.TokenDetailsVo;

import reactor.core.publisher.Mono;

import static com.teste.userserver.constants.AppConstants.ROLE;
import static com.teste.userserver.constants.AppConstants.USERNAME;
import static com.teste.userserver.constants.ErrorMessage.ACCOUNT_DISABLED;
import static com.teste.userserver.constants.ErrorMessage.PASSWORD_INVALID;
import static com.teste.userserver.constants.ErrorMessage.TOKEN_EXPIRED_WITH_MESSAGE;
import static com.teste.userserver.constants.ErrorMessage.TOKEN_INVALID;
import static com.teste.userserver.constants.ErrorMessage.TOKEN_INVALID_WITH_MESSAGE;
import static com.teste.userserver.constants.ErrorMessage.USERNAME_INVALID;
import static com.teste.userserver.constants.Message.USER_REGISTERED;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;

    @Value("${jwt.issuer}")
    private String issuer;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<User> registerUser(User user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER.name())
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .doOnSuccess(u -> log.info(USER_REGISTERED, u));
    }

    @Override
    public Mono<TokenDetailsVo> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(new InvalidCredentialsException(ACCOUNT_DISABLED));
                    }

                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new InvalidCredentialsException(PASSWORD_INVALID));
                    }

                    return Mono.just(generateToken(user).toBuilder()
                            .userId(user.getId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new InvalidCredentialsException(USERNAME_INVALID)));
    }

    @Override
    public Mono<UserValidationResponse> validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String subject = claims.getSubject();
            return userRepository.findById(subject)
                    .map(user -> UserValidationResponse.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .build());
        } catch (ExpiredJwtException e) {
            log.warn(TOKEN_EXPIRED_WITH_MESSAGE, e.getMessage());
            return Mono.error(e);
        } catch (JwtException e) {
            log.error(TOKEN_INVALID_WITH_MESSAGE, e.getMessage());
            return Mono.error(new InvalidCredentialsException(TOKEN_INVALID));
        }
    }

    private TokenDetailsVo generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLE, user.getRole());
        claims.put(USERNAME, user.getUsername());
        return generateToken(claims, user.getId());
    }

    private TokenDetailsVo generateToken(Map<String, Object> claims, String subject) {
        long expirationTimeInMillis = expirationInSeconds * 1500L;
        var expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims, subject);
    }

    private TokenDetailsVo generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        var createdDate = new Date();
        var token = Jwts.builder()
                .claims(claims)
                .issuer(issuer)
                .subject(subject)
                .issuedAt(createdDate)
                .id(UUID.randomUUID().toString())
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .compact();
        return TokenDetailsVo.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }
}
