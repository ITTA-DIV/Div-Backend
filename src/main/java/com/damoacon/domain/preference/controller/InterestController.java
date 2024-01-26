package com.damoacon.domain.preference.controller;

import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.interest.InterestSimpleDto;
import com.damoacon.domain.preference.service.InterestService;
import com.damoacon.global.common.ApiDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/interest")
public class InterestController {
    private final InterestService interestService;

    @PostMapping
    public ApiDataResponseDto<InterestSimpleDto> interestCreate(@RequestParam(name = "category")String category, @AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(interestService.createInterest(category,contextUser));
    }

    @DeleteMapping
    public ApiDataResponseDto heartDelete(@RequestParam(name = "category")String category, @AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(interestService.deleteInterest(category,contextUser));
    }
}
