package com.damoacon.domain.event.controller;


import com.damoacon.domain.event.dto.SearchRequestDto;
import com.damoacon.domain.event.dto.SearchResponseDto;
import com.damoacon.domain.event.repository.EventRepository;
import com.damoacon.domain.event.service.EventService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.List;

@SpringBootTest
@Log4j2
public class SearchTest {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventService eventService;

    @Test
    void SearchServiceTest() {
        SearchRequestDto s = SearchRequestDto.builder()
                .category_name("창업")
                .address("서울")
                .keywords("프린트 창업")
                .start_date(Timestamp.valueOf("2023-12-29 00:00:00"))
                .end_date(Timestamp.valueOf("2024-01-17 17:00:00"))
                .build();

        List<SearchResponseDto> searchResponseDtoList = eventService.getSearchEvents(s);
        searchResponseDtoList.stream().forEach(event -> {
            log.info("id: " + event.getId() + "\n" +
                    "title: " + event.getTitle() + "\n" +
                    "category: "+ event.getCategory_name() + "\n" +
                    "address: " + event.getAddress() + "\n" +
                    "date: " + event.getEventDateTimeString() + "\n" +
                    "location: " + event.getLocation());
        });
    }

//    @Test
//    void searchRepositoryTest() {
////        List<Event> eventList = eventRepository.findEventByKeyword("keyword");
////        List<Event> eventList = eventRepository.findEventByDate(Timestamp.valueOf("2024-01-02 00:00:00"), Timestamp.valueOf("2024-01-31 17:00:00"));
////        log.info(eventList);
////        List<Event> eventList = eventRepository.findEventsByAddress("서울");
////        List<Event> eventList = eventRepository.findEventsByPriceRange(40000L, 70000L);
////        log.info(eventList);
////        List<Event> eventList = eventRepository.findEventsByCategoryName("창업");
////        eventList.stream().forEach(event -> {
////            log.info(event);
////        });
////        List<Event> eventList = eventRepository.findEventsByType("커뮤니티");
////        log.info(eventList);
////        List<Event> eventList = eventRepository.findEventsByLocation("온라인");
////        log.info(eventList);
//    }
}
