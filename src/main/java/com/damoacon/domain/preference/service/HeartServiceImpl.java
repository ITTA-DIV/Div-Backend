package com.damoacon.domain.preference.service;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.event.repository.EventRepository;
import com.damoacon.domain.preference.dto.heart.EventSimpleDto;
import com.damoacon.domain.preference.dto.heart.HeartDto;
import com.damoacon.domain.preference.dto.heart.HeartSimpleDto;
import com.damoacon.domain.preference.dto.heart.MemberSimpleDto;
import com.damoacon.domain.preference.dto.interest.MyPageDto;
import com.damoacon.domain.preference.entity.Heart;
import com.damoacon.domain.preference.entity.Interest;
import com.damoacon.domain.preference.repository.HeartRepository;
import com.damoacon.domain.preference.repository.InterestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService{
    private final EventRepository eventRepository;
    private final HeartRepository heartRepository;
    public final InterestRepository interestRepository;
    @Override
    public HeartSimpleDto createHeart(long event_id, ContextUser contextUser){
        // 이벤트 찾기
        Optional<Event> optionalEvent = eventRepository.findById(event_id);
        Event event = optionalEvent.get();

        Long cnt = event.getHeartCount();
        cnt = cnt+1;

        // 사용자 찾기
        Member member = contextUser.getMember();

        // 이미 좋아요를 누른 경우 중단
        if (heartRepository.existsByMemberAndEvent(member, event)) {
            throw new IllegalStateException("이미 좋아요가 눌려 있습니다");
        }

        // 좋아요수 증가
        event.setHeartCount(cnt);
        Event updatedEvent = eventRepository.save(event);

        // 반환 형식
        HeartDto heartDto = new HeartDto();
        Heart newHeart = heartDto.toEntity(member, updatedEvent);
        Heart savedHeart = heartRepository.save(newHeart);
        return HeartSimpleDto.fromEntity(savedHeart);

    }

    public void deleteHeart(long event_id, ContextUser contextUser){
        // 이벤트 찾기
        Optional<Event> optionalEvent = eventRepository.findById(event_id);
        Event event = optionalEvent.get();

        // 사용자 찾기
        Member member = contextUser.getMember();

        // 좋아요 객체 찾기
        Heart existingHeart = heartRepository.findByMemberAndEvent(member, event);

        if (existingHeart != null) {
            // 좋아요 삭제
            heartRepository.delete(existingHeart);

            // 좋아요 수 감소
            Long cnt = event.getHeartCount();
            cnt = cnt-1;
            event.setHeartCount(cnt);
            eventRepository.save(event);
        } else {
            throw new EntityNotFoundException("좋아요가 존재하지 않음");
        }
    }

    public MyPageDto myPage(ContextUser contextUser){
        Member member = contextUser.getMember();

        // 회원 정보
        List<Interest> interests = interestRepository.findAllByMember(member);
        List<Category> categories = interests.stream()
                .map(Interest::getCategory)
                .collect(Collectors.toList());

        MemberSimpleDto memberSimpleDto = new MemberSimpleDto(member, categories);

        // 좋아요한 이벤트 개수
        int heartCount = heartRepository.countByMember(member);

        // 좋아요한 이벤트
        List<Heart> hearts = heartRepository.findByMemberOrderByIdDesc(member);
        List<Event> heartevents = hearts.stream()
                .map(Heart::getEvent)
                .collect(Collectors.toList());
        List<EventSimpleDto> hearteventsdto = heartevents.stream()
                .map(EventSimpleDto::fromEntity)
                .collect(Collectors.toList());

        // 반환 형태
        MyPageDto myPageDto = new MyPageDto(memberSimpleDto,heartCount,hearteventsdto);
        return myPageDto;
    }

}
