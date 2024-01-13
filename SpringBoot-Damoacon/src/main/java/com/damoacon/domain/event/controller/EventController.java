package com.damoacon.domain.event.controller;

import com.damoacon.domain.event.dto.MainEventResponseDto;
import com.damoacon.domain.event.dto.SearchRequestDto;
import com.damoacon.domain.event.dto.SearchResponseDto;
import com.damoacon.domain.event.service.EventService;
import com.damoacon.global.common.ApiDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ApiDataResponseDto<Page<SearchResponseDto>> searchEvents(SearchRequestDto searchRequestDto,
                                                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                                                    @RequestParam(name = "size", defaultValue = "18") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SearchResponseDto> searchResponseDtoList = eventService.getSearchEvents(searchRequestDto, pageable);
        return ApiDataResponseDto.of(searchResponseDtoList);
    }
}