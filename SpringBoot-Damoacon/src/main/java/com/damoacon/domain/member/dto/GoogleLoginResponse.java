package com.damoacon.domain.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleLoginResponse {
    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
