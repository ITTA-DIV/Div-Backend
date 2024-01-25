package com.damoacon.domain.preference.controller;

import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.heart.HeartSimpleDto;
import com.damoacon.domain.preference.service.HeartService;
import com.damoacon.global.common.ApiDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/heart")
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/{eventId}")
    public ApiDataResponseDto<HeartSimpleDto> heartCreate(@PathVariable Long eventId, @AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(heartService.createHeart(eventId, contextUser));
    }

    @DeleteMapping("/{eventId}")
    public ApiDataResponseDto heartDelete(@PathVariable Long eventId, @AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(heartService.deleteHeart(eventId,contextUser));
    }
}