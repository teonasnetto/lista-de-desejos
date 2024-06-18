package com.teste.userserver.dto.security;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegistrationResponse {

    private String id;

    private String username;

    private String role;

    private boolean enabled;

    private String createdAt;

    private String updatedAt;
}
