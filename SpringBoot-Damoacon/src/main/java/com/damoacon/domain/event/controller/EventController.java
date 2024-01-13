package com.damoacon.domain.event.controller;

import com.damoacon.domain.event.dto.MainEventResponseDto;
import com.damoacon.domain.event.dto.SearchRequestDto;
import com.damoacon.domain.event.dto.SearchResponseDto;
import com.damoacon.domain.event.service.EventService;
import com.damoacon.global.common.ApiDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ApiDataResponseDto<List<List<MainEventResponseDto>>> getMainEvents() {
        List<List<MainEventResponseDto>> mainEvents = eventService.getMainEvents();
        return ApiDataResponseDto.of(mainEvents);
    }

    @GetMapping("/search")
    public ApiDataResponseDto<List<SearchResponseDto>> searchEvents(SearchRequestDto searchRequestDto) {
        List<SearchResponseDto> searchResponseDtoList = eventService.getSearchEvents(searchRequestDto);
        return ApiDataResponseDto.of(searchResponseDtoList);
    }
}