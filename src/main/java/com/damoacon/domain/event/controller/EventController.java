package com.damoacon.domain.event.controller;

import com.damoacon.domain.event.dto.DetailEventResponseDto;
import com.damoacon.domain.event.dto.MainEventResponseDto;
import com.damoacon.domain.event.dto.SearchRequestDto;
import com.damoacon.domain.event.dto.SearchResponseDto;
import com.damoacon.domain.event.service.EventService;
import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.comment.CommentResponseDto;
import com.damoacon.global.common.ApiDataResponseDto;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ApiDataResponseDto<List<List<MainEventResponseDto>>> getMainEvents() {

        return ApiDataResponseDto.of(eventService.getMainEvents());
    }
  
    @GetMapping("/search")
    public ApiDataResponseDto<Page<SearchResponseDto>> searchEvents(SearchRequestDto searchRequestDto,
                                                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                                                    @RequestParam(name = "size", defaultValue = "18") int size) {

        return ApiDataResponseDto.of(eventService.getSearchEvents(searchRequestDto, PageRequest.of(page, size)));
    }
  
    @GetMapping("/{eventId}")
    public ApiDataResponseDto<DetailEventResponseDto> getDetailEvent(@PathVariable Long eventId) {

        return ApiDataResponseDto.of(eventService.getDetailEvent(eventId));
    }

    @GetMapping("/comment/{eventId}")
    public ApiDataResponseDto<List<CommentResponseDto>> getComments(@PathVariable Long eventId, @Null @AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(eventService.getComments(eventId, contextUser != null ? contextUser.getMember() : null));
    }
}