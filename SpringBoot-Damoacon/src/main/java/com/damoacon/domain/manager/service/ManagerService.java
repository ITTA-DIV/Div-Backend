package com.damoacon.domain.manager.service;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.event.repository.CategoryRepository;
import com.damoacon.domain.event.repository.EventRepository;
import com.damoacon.domain.manager.dto.EventCreateDto;
import com.damoacon.domain.manager.dto.EventResponseDto;
import com.damoacon.domain.manager.repository.EventManagerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventManagerRepository eventManagerRepository;

    public EventResponseDto eventCreate(EventCreateDto requestDto){
        try {
            String cate= requestDto.getCategory();
            Map<String, Long> categoryMapping = new HashMap<>();
            categoryMapping.put("창업", 1l);
            categoryMapping.put("IT/프로그래밍", 2l);
            categoryMapping.put("라이프", 3l);
            categoryMapping.put("경제/금융", 4l);
            categoryMapping.put("경영", 5l);
            categoryMapping.put("인문/사회", 6l);
            categoryMapping.put("예술", 7l);
            categoryMapping.put("마케팅", 8l);
            categoryMapping.put("커리어", 9l);
            categoryMapping.put("과학기술", 10l);
            categoryMapping.put("디자인/영상", 11l);
            categoryMapping.put("의료/의학", 12l);
            categoryMapping.put("행사 기획", 13l);
            categoryMapping.put("관광/여행", 14l);
            categoryMapping.put("기타", 15l);
            Long categoryId = categoryMapping.get(cate);

            Optional<Category> cateOptional = categoryRepository.findById(categoryId);
            Category category = cateOptional.orElseThrow(() -> new EntityNotFoundException("Categoty not found with id: " + cate));

            Event newEvent = eventRepository.save(requestDto.toEntity(category));
            return EventResponseDto.fromEntity(newEvent);
        } catch (Exception e) {
            throw new IllegalArgumentException("이벤트 생성 실패");
        }
    }
    public List<EventResponseDto> eventApplyList(){
        List<Event> unpermittedEvents = eventManagerRepository.findNotPermit();
        return unpermittedEvents.stream()
                .map(EventResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public EventResponseDto eventApplyPermit(Long event_id){
        Optional<Event> optionalEvent = eventRepository.findById(event_id);

        Event event = optionalEvent.get();
        event.setIs_permit(1);

        Event updatedEvent = eventRepository.save(event);

        return EventResponseDto.fromEntity(updatedEvent);
    }
}
