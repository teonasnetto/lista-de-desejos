package com.teste.userserver.vo;

import io.jsonwebtoken.Claims;

public record VerificationResultVo(Claims claims, String token) {

}
