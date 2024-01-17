package com.damoacon.domain.preference.service;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.*;
import com.damoacon.domain.event.repository.EventRepository;
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
        Optional<Event> optionalEvent = eventRepository.findById(event_id);
        Event event = optionalEvent.get();

        Long cnt = event.getHeartCount();
        cnt = cnt+1;



        Member member = contextUser.getMember();
        if (heartRepository.existsByMemberAndEvent(member, event)) {
            throw new IllegalStateException("이미 좋아요 눌렀음");
        }

        event.setHeartCount(cnt);
        Event updatedEvent = eventRepository.save(event);

        HeartDto heartDto = new HeartDto();
        Heart newHeart = heartDto.toEntity(member, updatedEvent);
        Heart savedHeart = heartRepository.save(newHeart);
        return HeartSimpleDto.fromEntity(savedHeart);

    }

    public void deleteHeart(long event_id, ContextUser contextUser){
        Optional<Event> optionalEvent = eventRepository.findById(event_id);
        Event event = optionalEvent.get();
        Member member = contextUser.getMember();
        Long cnt = event.getHeartCount();
        cnt = cnt-1;
        event.setHeartCount(cnt);
        Event updatedEvent = eventRepository.save(event);

        Heart existingHeart = heartRepository.findByMemberAndEvent(member, updatedEvent);

        if (existingHeart != null) {
            heartRepository.delete(existingHeart);
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
