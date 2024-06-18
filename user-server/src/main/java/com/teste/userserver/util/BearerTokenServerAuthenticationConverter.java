package com.teste.userserver.util;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;

import com.teste.userserver.dto.security.CustomPrincipal;
import com.teste.userserver.vo.VerificationResultVo;

import reactor.core.publisher.Mono;

import static com.teste.userserver.constants.AppConstants.ROLE;
import static com.teste.userserver.constants.AppConstants.USERNAME;

import java.util.List;

@RequiredArgsConstructor
public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return extractHeader(exchange)
                .flatMap(authValue -> {
                    var token = authValue.substring(BEARER_PREFIX.length());
                    return jwtUtil.getVerificationResult(token)
                            .flatMap(this::createToken);
                });
    }

    private Mono<String> extractHeader(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }

    private Mono<Authentication> createToken(VerificationResultVo verificationResultVo) {
        Claims claims = verificationResultVo.claims();
        String subject = claims.getSubject();
        var role = claims.get(ROLE, String.class);
        var username = claims.get(USERNAME, String.class);
        var authorities = List.of(new SimpleGrantedAuthority(role));
        var principal = new CustomPrincipal(subject, username);

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
    }
}
