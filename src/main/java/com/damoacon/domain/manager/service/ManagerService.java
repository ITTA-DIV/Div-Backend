package com.damoacon.domain.manager.service;

import com.damoacon.domain.manager.dto.EventCreateDto;
import com.damoacon.domain.manager.dto.EventResponseDto;

import java.util.List;

public interface ManagerService {
    EventResponseDto eventCreate(EventCreateDto requestDto);

    List<EventResponseDto> eventApplyList();

    EventResponseDto eventApplyPermit(Long eventId);
}
