package com.damoacon.domain.event.service;

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
                    dto.setHostProfile(event.getHostProfile());
                    // Category가 null이 아니라면 categoryName 설정
                    if (event.getCategory() != null) {
                        dto.setCategory_name(event.getCategory().getCategory_name());
                    }
                    // applyEndDate와 현재 날짜를 비교하여 남은 날짜 계산
                    if (event.getApplyEndDate() != null) {
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        LocalDateTime applyEndDateTime = event.getApplyEndDate().toLocalDateTime();
                        long remainingDays = ChronoUnit.DAYS.between(currentDateTime, applyEndDateTime);
                        // 남은 날짜를 DTO에 설정
                        dto.setRemainingDays((int) remainingDays);
                    }
                    if (event.getStartDate() != null) {
                        // event의 startdate를 원하는 형식의 문자열로 변환하여 DTO에 설정
                        Timestamp timestamp = event.getStartDate();
                        // timestamp to localdatetime
                        LocalDateTime localDateTime = timestamp.toLocalDateTime();
                        String formattedDateTime = localDateTime.format(formatter);

                        dto.setEventDateTimeString(formattedDateTime);
                    }
                    // event의 price가 무료이면 1, 아니면 0을 반환
                    dto.setIsFree("무료".equals(event.getPrice()) ? 1 : 0);

                    return dto;
                })
                .collect(Collectors.toList());
    }
}