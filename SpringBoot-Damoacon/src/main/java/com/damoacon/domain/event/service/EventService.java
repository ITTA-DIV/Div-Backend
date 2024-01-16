package com.damoacon.domain.event.service;

import com.damoacon.domain.event.dto.DetailEventResponseDto;
import com.damoacon.domain.event.dto.MainEventResponseDto;
import com.damoacon.global.exception.GeneralException;

import java.util.List;

public interface EventService {
    List<List<MainEventResponseDto>> getMainEvents();

    DetailEventResponseDto getDetailEvent(Long eventId) throws GeneralException;
}
