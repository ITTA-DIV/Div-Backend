package com.damoacon.domain.event.service;

import com.damoacon.domain.event.dto.SearchRequestDto;
import com.damoacon.domain.event.dto.SearchResponseDto;
import com.damoacon.domain.event.repository.EventRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;

@SpringBootTest
@Log4j2
public class EventServiceTest {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventService eventService;

    @Test
    @DisplayName("getSearchEvents() Test")
    void getSearchEventTest() {
        SearchRequestDto s = SearchRequestDto.builder()
                .category_name("창업")
                .address("서울")
                .keywords("프린트 창업")
                .start_date(Timestamp.valueOf("2023-12-29 00:00:00"))
                .end_date(Timestamp.valueOf("2024-01-17 17:00:00"))
                .build();

        Pageable pageable = PageRequest.of(0, 18);
        Page<SearchResponseDto> searchResponseDtoList = eventService.getSearchEvents(s, pageable);
        searchResponseDtoList.getContent().stream().forEach(event -> log.info("id: " + event.getId() + "\n" +
                "title: " + event.getTitle() + "\n" +
                "category: "+ event.getCategory_name() + "\n" +
                "address: " + event.getAddress() + "\n" +
                "date: " + event.getEventDateTimeString() + "\n" +
                "location: " + event.getLocation()));
    }
}
