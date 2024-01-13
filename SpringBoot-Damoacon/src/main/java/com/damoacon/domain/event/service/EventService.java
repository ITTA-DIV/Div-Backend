package com.damoacon.domain.event.service;

import com.damoacon.domain.event.dto.MainEventResponseDto;
import com.damoacon.domain.event.dto.SearchRequestDto;
import com.damoacon.domain.event.dto.SearchResponseDto;

import java.util.List;

public interface EventService {
    List<List<MainEventResponseDto>> getMainEvents();

    List<SearchResponseDto> getSearchEvents(SearchRequestDto searchRequestDto);
}
