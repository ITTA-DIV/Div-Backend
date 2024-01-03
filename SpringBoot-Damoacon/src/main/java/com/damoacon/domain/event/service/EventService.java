package com.damoacon.domain.event.service;

import com.damoacon.domain.event.dto.MainEventResponseDto;

import java.util.List;

public interface EventService {
    List<List<MainEventResponseDto>> getMainEvents();
}
