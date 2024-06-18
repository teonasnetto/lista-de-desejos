package com.teste.userserver.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetailsVo {

    private String userId;

    private String token;

    private Date issuedAt;

    private Date expiresAt;
}
