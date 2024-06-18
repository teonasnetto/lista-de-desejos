package com.teste.userserver.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Principal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal implements Principal {

    private String id;

    private String name;
}
