package com.damoacon.domain.event.service;

import com.damoacon.domain.event.dto.MainEventResponseDto;
import com.damoacon.domain.event.dto.SearchRequestDto;
import com.damoacon.domain.event.dto.SearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {
    List<List<MainEventResponseDto>> getMainEvents();

    Page<SearchResponseDto> getSearchEvents(SearchRequestDto searchRequestDto, Pageable pageable);
}
