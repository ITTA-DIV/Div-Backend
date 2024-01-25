package com.damoacon.domain.preference.service;

import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.event.repository.EventRepository;
import com.damoacon.domain.preference.dto.heart.HeartDto;
import com.damoacon.domain.preference.dto.heart.HeartSimpleDto;
import com.damoacon.domain.preference.entity.Heart;
import com.damoacon.domain.preference.repository.HeartRepository;
import com.damoacon.global.constant.ErrorCode;
import com.damoacon.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService {
    private final EventRepository eventRepository;
    private final HeartRepository heartRepository;

    @Override
    @Transactional
    public HeartSimpleDto createHeart(long eventId, ContextUser contextUser) {
        // 이벤트 찾기
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new GeneralException(ErrorCode.EVENT_NOT_FOUND));

        Long cnt = event.getHeartCount();
        cnt = cnt+1;

        // 사용자 찾기
        Member member = contextUser.getMember();

        // 이미 좋아요를 누른 경우 중단
        if (heartRepository.existsByMemberAndEvent(member, event)) {
            throw new IllegalStateException("이미 좋아요가 눌려 있습니다");
        }

        // 좋아요 수 증가
        event.setHeartCount(cnt);
        Event updatedEvent = eventRepository.save(event);

        // 반환 형식
        HeartDto heartDto = new HeartDto();
        Heart newHeart = heartDto.toEntity(member, updatedEvent);
        Heart savedHeart = heartRepository.save(newHeart);

        return HeartSimpleDto.fromEntity(savedHeart);
    }

    @Override
    @Transactional
    public long deleteHeart(long eventId, ContextUser contextUser) {
        // 이벤트 찾기
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new GeneralException(ErrorCode.EVENT_NOT_FOUND));

        // 사용자 찾기
        Member member = contextUser.getMember();

        // 좋아요 객체 찾기
        Heart existingHeart = heartRepository.findByMemberAndEvent(member, event).orElseThrow(() -> new GeneralException(ErrorCode.HEART_NOT_FOUND));

        // 좋아요 삭제
        heartRepository.delete(existingHeart);

        // 좋아요 수 감소
        Long cnt = event.getHeartCount();
        cnt = cnt-1;
        event.setHeartCount(cnt);
        eventRepository.save(event);

        return eventId;
    }
}
