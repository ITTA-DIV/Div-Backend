package com.damoacon.domain.manager.controller;

import com.damoacon.domain.manager.dto.EventCreateDto;
import com.damoacon.domain.manager.dto.EventResponseDto;
import com.damoacon.domain.manager.service.ManagerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.damoacon.global.common.ApiDataResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager")
public class ManagerController {
    private final ManagerServiceImpl managerService;

    @PostMapping
    public ApiDataResponseDto<EventResponseDto> save(@RequestBody EventCreateDto requestDto){

        return ApiDataResponseDto.of(managerService.eventCreate(requestDto));
    }

    @GetMapping
    public ApiDataResponseDto<List<EventResponseDto>> applyList() {

        return ApiDataResponseDto.of(managerService.eventApplyList());
    }

    @PatchMapping("/{eventId}")
    public ApiDataResponseDto<EventResponseDto> changePermit(@PathVariable Long eventId) {

        return ApiDataResponseDto.of(managerService.eventApplyPermit(eventId));
    }
}
