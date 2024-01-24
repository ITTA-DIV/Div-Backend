package com.damoacon.domain.preference.controller;

import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.heart.HeartSimpleDto;
import com.damoacon.domain.preference.dto.interest.MyPageDto;
import com.damoacon.domain.preference.service.HeartService;
import com.damoacon.global.common.ApiDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/heart")
public class HeartController {
    private final HeartService heartService;
    @PostMapping ("/{event_id}")
    public ApiDataResponseDto<HeartSimpleDto> heartCreate(@PathVariable Long event_id, @AuthenticationPrincipal ContextUser contextUser){
        try {
            return ApiDataResponseDto.of(heartService.createHeart(event_id,contextUser));
        } catch (Exception e) {
            throw new IllegalArgumentException("좋아요 누르기 실패");
        }
    }
    @DeleteMapping ("/{event_id}")
    public ApiDataResponseDto<HeartSimpleDto> heartDelete(@PathVariable Long event_id, @AuthenticationPrincipal ContextUser contextUser){
        try {
            heartService.deleteHeart(event_id,contextUser);
            return ApiDataResponseDto.empty();
        } catch (Exception e) {
            throw new IllegalArgumentException("좋아요 취소 실패");
        }
    }
    @GetMapping ("mypage")
    public ApiDataResponseDto<MyPageDto> myPage(@AuthenticationPrincipal ContextUser contextUser){
        try {
            return ApiDataResponseDto.of(heartService.myPage(contextUser));
        } catch (Exception e) {
            throw new IllegalArgumentException("마이페이지 조회 실패");
        }
    }
}