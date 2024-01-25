package com.damoacon.domain.event.service;

import com.damoacon.domain.event.dto.DetailEventResponseDto;
import com.damoacon.domain.event.dto.MainEventResponseDto;
import com.damoacon.domain.event.dto.SearchRequestDto;
import com.damoacon.domain.event.dto.SearchResponseDto;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.preference.dto.comment.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.damoacon.global.exception.GeneralException;

import java.util.List;

public interface EventService {
    List<List<MainEventResponseDto>> getMainEvents();

    Page<SearchResponseDto> getSearchEvents(SearchRequestDto searchRequestDto, Pageable pageable);

    DetailEventResponseDto getDetailEvent(Long eventId) throws GeneralException;

    public List<CommentResponseDto> getComments(Long eventId, Member member);
}
