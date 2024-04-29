package com.teste.userserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static com.teste.userserver.constants.ErrorMessage.ACCESS_DENIED;
import static com.teste.userserver.constants.ErrorMessage.ACCOUNT_DISABLED;
import static com.teste.userserver.constants.ErrorMessage.UNAUTHORIZED_WITH_MESSAGE;
import static com.teste.userserver.constants.PathConstants.BEARER_SERVER_WEB_EXCHANGE_MATCHERS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import com.teste.userserver.dto.security.CustomPrincipal;
import com.teste.userserver.exception.InvalidCredentialsException;
import com.teste.userserver.model.User;
import com.teste.userserver.repository.UserRepository;
import com.teste.userserver.util.BearerTokenServerAuthenticationConverter;
import com.teste.userserver.util.JwtUtil;

import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig implements ReactiveAuthenticationManager {

    @Value("${jwt.secret}")
    private String secret;

    private final UserRepository userRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        var principal = (CustomPrincipal) authentication.getPrincipal();
        return userRepository.findById(principal.getId())
                .filter(User::isEnabled)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException(ACCOUNT_DISABLED)))
                .map(user -> authentication);
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter bearerAuthenticationFilter = createBearerAuthenticationFilter(secret);
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(auth -> auth
                        .anyExchange()
                        .permitAll())
                .exceptionHandling(ex -> ex.authenticationEntryPoint((swe, e) -> {
                    log.error(UNAUTHORIZED_WITH_MESSAGE, e.getMessage());
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                }).accessDeniedHandler((swe, e) -> {
                    log.error(ACCESS_DENIED, e.getMessage());
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                }))
                .addFilterAt(bearerAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private AuthenticationWebFilter createBearerAuthenticationFilter(String secret) {
        var bearerAuthenticationFilter = new AuthenticationWebFilter(this);
        bearerAuthenticationFilter.setServerAuthenticationConverter(
                new BearerTokenServerAuthenticationConverter(new JwtUtil(secret)));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers
                .pathMatchers(BEARER_SERVER_WEB_EXCHANGE_MATCHERS));
        return bearerAuthenticationFilter;
    }
}
