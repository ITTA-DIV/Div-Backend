package com.damoacon.domain.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerCheckDto {
    private Long id;
    private String username;
    private boolean isManager;
}
