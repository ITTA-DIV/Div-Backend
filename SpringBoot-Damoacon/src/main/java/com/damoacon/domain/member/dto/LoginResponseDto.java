package com.damoacon.domain.member.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private Long memberId;
    private String accessToken;
    private String refreshToken;
}
