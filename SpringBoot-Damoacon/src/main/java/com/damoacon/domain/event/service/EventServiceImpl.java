package com.damoacon.domain.event.service;

import com.damoacon.domain.event.dto.DetailEventResponseDto;
import com.damoacon.domain.event.dto.MainEventResponseDto;
import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.event.repository.EventRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<List<MainEventResponseDto>> getMainEvents() {
        Timestamp eightDaysAfter = Timestamp.valueOf(LocalDateTime.now().plusDays(8));  // 현재 시간을 기준으로 8일을 더하기

        // 현재 시간을 기준으로 8일 이내의 이벤트 리스트 가져오기
        List<Event> eventsWithinEightDays = eventRepository.findEventsWithinEightDays(eightDaysAfter);
        List<MainEventResponseDto> dtoListWithinEightDays = mapEventListToDtoList(eventsWithinEightDays);

        // 최상위 12개의 이벤트 리스트 가져오기
        List<Event> last12Events = eventRepository.findLast12Events();
        List<MainEventResponseDto> dtoListLast12Events = mapEventListToDtoList(last12Events);

        // 좋아요 수 기준으로 상위 12개의 이벤트 리스트 가져오기
        List<Event> top12EventsByHeartCount = eventRepository.findTop12EventsByHeartCount();
        List<MainEventResponseDto> dtoListTop12Events = mapEventListToDtoList(top12EventsByHeartCount);

        // 각각의 DTO 리스트를 리스트로 묶어 반환
        return Arrays.asList(dtoListWithinEightDays, dtoListLast12Events, dtoListTop12Events);
    }

    @Override
    @Transactional(readOnly = true)
    public DetailEventResponseDto getDetailEvent(Long eventId) throws IllegalArgumentException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 없습니다. eventId = " + eventId));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일(E) HH:mm");

        return DetailEventResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .thumbnail(event.getThumbnail())
                .categoryId(event.getCategory().getId())
                .categoryName(event.getCategory().getCategory_name())
                .price(event.getPrice())
                .link(event.getLink())
                .location(event.getLocation())
                .address(event.getAddress())
                .host(event.getHost())
                .hostProfile(event.getHostProfile())
                .eventApplyStartDate(event.getApplyStartDate().toLocalDateTime().format(formatter))
                .eventApplyEndDate(event.getApplyEndDate().toLocalDateTime().format(formatter))
                .eventStartDate(event.getStartDate().toLocalDateTime().format(formatter))
                .eventEndDate(event.getEnd_date().toLocalDateTime().format(formatter))
                .build();
    }

    // entity to dto
    private List<MainEventResponseDto> mapEventListToDtoList(List<Event> events) {
        // 원하는 event startDate 문자열 형식
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일 (E)");

        return events.stream()
                .map(event -> {
                    MainEventResponseDto dto = new MainEventResponseDto();
                    dto.setId(event.getId());
                    dto.setTitle(event.getTitle());
                    dto.setThumbnail(event.getThumbnail());
                    dto.setHost(event.getHost());
                    dto.setHostProfile(event.getHostProfile());
                    dto.setCategory_name(event.getCategory().getCategory_name());
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    LocalDateTime applyEndDateTime = event.getApplyEndDate().toLocalDateTime();
                    long remainingDays = ChronoUnit.DAYS.between(currentDateTime, applyEndDateTime);
                    dto.setRemainingDays((int) remainingDays);
                    Timestamp timestamp = event.getStartDate();
                    LocalDateTime localDateTime = timestamp.toLocalDateTime();  // timestamp to localdatetime
                    String formattedDateTime = localDateTime.format(formatter);
                    dto.setEventDateTimeString(formattedDateTime);
                    dto.setIsFree("무료".equals(event.getPrice()) ? 1 : 0);   // event의 price가 무료이면 1, 아니면 0을 반환

                    return dto;
                })
                .collect(Collectors.toList());
    }
}
