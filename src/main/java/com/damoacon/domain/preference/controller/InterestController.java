package com.damoacon.domain.preference.controller;

import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.interest.InterestSimpleDto;
import com.damoacon.domain.preference.service.InterestService;
import com.damoacon.global.common.ApiDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/interest")
public class InterestController {
    private final InterestService interestService;
    @PostMapping("create")
    public ApiDataResponseDto<InterestSimpleDto> interestCreate(@RequestParam(name = "category") String category, @AuthenticationPrincipal ContextUser contextUser){
        try {
            return ApiDataResponseDto.of(interestService.createInterest(category,contextUser));
        } catch (Exception e) {
            throw new IllegalArgumentException("관심분야 등록 실패");
        }
    }
    @DeleteMapping("delete")
    public ApiDataResponseDto<InterestSimpleDto> heartDelete(@RequestParam(name = "category") String category, @AuthenticationPrincipal ContextUser contextUser){
        try {
            interestService.deleteInterest(category,contextUser);
            return ApiDataResponseDto.empty();
        } catch (Exception e) {
            throw new IllegalArgumentException("관심분야 취소 실패");
        }
    }
}
