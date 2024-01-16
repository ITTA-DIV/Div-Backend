package com.damoacon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenClaimVo {
    private final Long id;
    private final String email;
}
