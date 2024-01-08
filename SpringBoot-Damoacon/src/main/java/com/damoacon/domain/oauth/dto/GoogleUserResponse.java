package com.damoacon.domain.oauth.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserResponse {
    private String email;
    private String name;
    private String picture;
}
