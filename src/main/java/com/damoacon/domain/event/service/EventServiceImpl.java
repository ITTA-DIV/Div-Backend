package com.damoacon.domain.event.service;

import com.damoacon.domain.event.dto.DetailEventResponseDto;
import com.damoacon.domain.event.dto.MainEventResponseDto;
import com.damoacon.domain.event.dto.SearchRequestDto;
import com.damoacon.domain.event.dto.SearchResponseDto;
import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.event.repository.EventRepository;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.preference.dto.comment.CommentResponseDto;
import com.damoacon.domain.preference.entity.Comment;
import com.damoacon.domain.preference.repository.CommentRepository;
import com.damoacon.global.constant.ErrorCode;
import com.damoacon.global.exception.GeneralException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<List<MainEventResponseDto>> getMainEvents() {
        Timestamp eightDaysAfter = Timestamp.valueOf(LocalDateTime.now().plusDays(8));  // 현재 시간을 기준으로 8일을 더하기

        // 현재 시간을 기준으로 8일 이내의 이벤트 리스트
        List<Event> eventsWithinEightDays = eventRepository.findEventsWithinEightDays(eightDaysAfter);
        List<MainEventResponseDto> dtoListWithinEightDays = mapEventListToDtoList(eventsWithinEightDays);

        // 최상위 12개의 이벤트 리스트
        List<Event> last12Events = eventRepository.findLast12Events();
        List<MainEventResponseDto> dtoListLast12Events = mapEventListToDtoList(last12Events);

        // 좋아요 수 기준으로 상위 12개의 이벤트 리스트
        List<Event> top12EventsByHeartCount = eventRepository.findTop12EventsByHeartCount();
        List<MainEventResponseDto> dtoListTop12Events = mapEventListToDtoList(top12EventsByHeartCount);

        return Arrays.asList(dtoListWithinEightDays, dtoListLast12Events, dtoListTop12Events);
    }

    /**
     * 검색 필터링
     *
     * @param searchRequestDto - 검색 요청 DTO
     * @param pageable
     * @return 검색 결과 DTO 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SearchResponseDto> getSearchEvents(SearchRequestDto searchRequestDto, Pageable pageable) {
        // 검색 결과 이벤트 리스트 초기화
        List<Event> resultEvents = new ArrayList<>();

        // 키워드 검색
        if (searchRequestDto.getKeywords() != null) {
            // 키워드를 공백 기준으로 나눠 리스트로 만듦
            List<String> k_List = List.of(searchRequestDto.getKeywords().split(" "));
            // 각 키워드에 대해 이벤트를 검색하고 결과 리스트에 추가
            k_List.forEach(k -> resultEvents.addAll(eventRepository.findEventByKeyword(k)));
            // 결과 리스트가 비어있으면 빈 DTO 리스트 반환
            if (resultEvents.isEmpty()) {
                return searchEventListToDtoPage(resultEvents, pageable);
            }
        }

        // 날짜 범위 검색
        if (searchRequestDto.getStart_date() != null && searchRequestDto.getEnd_date() != null) {
            // 날짜 범위에 해당하는 이벤트를 검색
            List<Event> dateEvents = eventRepository.findEventByDate(
                    searchRequestDto.getStart_date(),
                    searchRequestDto.getEnd_date());
            // 결과 리스트가 비어있지 않으면 결과 리스트를 날짜 이벤트 리스트로 갱신
            if (!resultEvents.isEmpty()) {
                resultEvents.retainAll(dateEvents);
            } else {
                resultEvents.addAll(dateEvents);
            }
            // 결과 리스트가 비어있으면 빈 DTO 리스트 반환
            if (resultEvents.isEmpty()) {
                return searchEventListToDtoPage(resultEvents, pageable);
            }
        }

        // 주소 검색
        if (searchRequestDto.getAddress() != null) {
            // 주소에 해당하는 이벤트를 검색
            List<Event> addressEvents = eventRepository.findEventsByAddress(searchRequestDto.getAddress());
            // 결과 리스트가 비어있지 않으면 결과 리스트를 주소 이벤트 리스트로 갱신
            if (!resultEvents.isEmpty()) {
                resultEvents.retainAll(addressEvents);
            } else {
                resultEvents.addAll(addressEvents);
            }
            // 결과 리스트가 비어있으면 빈 DTO 리스트 반환
            if (resultEvents.isEmpty()) {
                return searchEventListToDtoPage(resultEvents, pageable);
            }
        }

        // 무료 이벤트 검색
        if (searchRequestDto.isFree()) {
            // 무료 이벤트를 검색
            List<Event> freeEvents = eventRepository.findEventsByFreePrice();
            // 결과 리스트가 비어있지 않으면 결과 리스트를 무료 이벤트 리스트로 갱신
            if (!resultEvents.isEmpty()) {
                resultEvents.retainAll(freeEvents);
            } else {
                resultEvents.addAll(freeEvents);
            }
            // 결과 리스트가 비어있으면 빈 DTO 리스트 반환
            if (resultEvents.isEmpty()) {
                return searchEventListToDtoPage(resultEvents, pageable);
            }
        }

        // 유료 이벤트 검색
        if (searchRequestDto.isNotFree()) {
            // 유료 이벤트를 검색
            List<Event> notFreeEvents = eventRepository.findEventsByNotFreePrice();
            // 결과 리스트가 비어있지 않으면 결과 리스트를 유료 이벤트 리스트로 갱신
            if (!resultEvents.isEmpty()) {
                resultEvents.retainAll(notFreeEvents);
            } else {
                resultEvents.addAll(notFreeEvents);
            }
            // 결과 리스트가 비어있으면 빈 DTO 리스트 반환
            if (resultEvents.isEmpty()) {
                return searchEventListToDtoPage(resultEvents, pageable);
            }
        }

        // 가격 범위 검색
        if (searchRequestDto.getMinPrice() != null && searchRequestDto.getMaxPrice() != null) {
            // 가격 범위에 해당하는 이벤트를 검색
            List<Event> priceRangeEvents = eventRepository.findEventsByPriceRange(
                    searchRequestDto.getMinPrice(),
                    searchRequestDto.getMaxPrice());
            // 결과 리스트가 비어있지 않으면 결과 리스트를 가격 범위 이벤트 리스트로 갱신
            if (!resultEvents.isEmpty()) {
                resultEvents.retainAll(priceRangeEvents);
            } else {
                resultEvents.addAll(priceRangeEvents);
            }
            // 결과 리스트가 비어있으면 빈 DTO 리스트 반환
            if (resultEvents.isEmpty()) {
                return searchEventListToDtoPage(resultEvents, pageable);
            }
        }

        // 위치 검색
        if (searchRequestDto.getLocation() != null) {
            // 위치에 해당하는 이벤트를 검색
            List<Event> locationEvents = eventRepository.findEventsByLocation(searchRequestDto.getLocation());
            // 결과 리스트가 비어있지 않으면 결과 리스트를 위치 이벤트 리스트로 갱신
            if (!resultEvents.isEmpty()) {
                resultEvents.retainAll(locationEvents);
            } else {
                resultEvents.addAll(locationEvents);
            }
            // 결과 리스트가 비어있으면 빈 DTO 리스트 반환
            if (resultEvents.isEmpty()) {
                return searchEventListToDtoPage(resultEvents, pageable);
            }
        }

        // 타입 검색
        if (searchRequestDto.getType() != null) {
            // 타입에 해당하는 이벤트를 검색
            List<Event> typeEvents = eventRepository.findEventsByType(searchRequestDto.getType());
            // 결과 리스트가 비어있지 않으면 결과 리스트를 타입 이벤트 리스트로 갱신
            if (!resultEvents.isEmpty()) {
                resultEvents.retainAll(typeEvents);
            } else {
                resultEvents.addAll(typeEvents);
            }
            // 결과 리스트가 비어있으면 빈 DTO 리스트 반환
            if (resultEvents.isEmpty()) {
                return searchEventListToDtoPage(resultEvents, pageable);
            }
        }

        // 카테고리 검색
        if (searchRequestDto.getCategory_name() != null) {
            // 카테고리에 해당하는 이벤트를 검색
            List<Event> categoryEvents = eventRepository.findEventsByCategoryName(searchRequestDto.getCategory_name());
            // 결과 리스트가 비어있지 않으면 결과 리스트를 카테고리 이벤트 리스트로 갱신
            if (!resultEvents.isEmpty()) {
                resultEvents.retainAll(categoryEvents);
            } else {
                resultEvents.addAll(categoryEvents);
            }
            // 결과 리스트가 비어있으면 빈 DTO 리스트 반환
            if (resultEvents.isEmpty()) {
                return searchEventListToDtoPage(resultEvents, pageable);
            }
        }

        // 결과 이벤트 리스트를 SearchResponseDto로 변환
        return searchEventListToDtoPage(resultEvents, pageable);
    }

    /**
     * 이벤트 상세화면 조회
     * @param eventId 이벤트 번호를 통해 이벤트 조회
     * @return
     * @throws IllegalArgumentException 없는 번호일 경우 예외 처리
     */
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
                .eventStartDate(event.getStart_date().toLocalDateTime().format(formatter))
                .eventEndDate(event.getEnd_date().toLocalDateTime().format(formatter))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long eventId, Member member) {
        List<Comment> comments = commentRepository.findCommentsByEventId(validateEvent(eventId).getId());

        return comments.stream()
                .map(comment -> toResponse(comment, member))
                .collect(Collectors.toList());
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
                    dto.setRemainingDays((int) ChronoUnit.DAYS.between(LocalDateTime.now(), event.getApplyEndDate().toLocalDateTime()));
                    dto.setEventDateTimeString(event.getStart_date().toLocalDateTime().format(formatter));
                    dto.setIsFree("무료".equals(event.getPrice()) ? 1 : 0);   // event의 price가 무료이면 1, 아니면 0을 반환

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // entity to dto
    private Page<SearchResponseDto> searchEventListToDtoPage(List<Event> events, Pageable pageable) {
        // 수동으로 날짜순으로 정렬.
        events.sort(Comparator.comparing(Event::getStart_date));
        // 원하는 event startDate 문자열 형식
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일 (E)");

        List<SearchResponseDto> searchResponseDtoList = events.stream()
                .map(event -> {
                    SearchResponseDto dto = new SearchResponseDto();
                    dto.setId(event.getId());
                    dto.setTitle(event.getTitle());
                    dto.setThumbnail(event.getThumbnail());
                    dto.setHost(event.getHost());
                    dto.setHostProfile(event.getHostProfile());
                    dto.setCategory_name(event.getCategory().getCategory_name());
                    dto.setLocation(event.getLocation());
                    dto.setAddress(event.getAddress());
                    dto.setType(event.getType());
                    dto.setRemainingDays((int) ChronoUnit.DAYS.between(LocalDateTime.now(), event.getApplyEndDate().toLocalDateTime()));
                    dto.setEventDateTimeString(event.getStart_date().toLocalDateTime().format(formatter));
                    dto.setIsFree("무료".equals(event.getPrice()) ? 1 : 0);   // event의 price가 무료이면 1, 아니면 0을 반환

                    return dto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(searchResponseDtoList, pageable, searchResponseDtoList.size());
    }

    private Event validateEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new GeneralException(ErrorCode.EVENT_NOT_FOUND));

        return event;
    }

    private CommentResponseDto toResponse(Comment comment, Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");

        boolean isMine = false;
        if (member != null) isMine = member.getId().equals(comment.getMember().getId());

        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .creationTime(comment.getLastModifiedTime().format(formatter))
                .writerId(comment.getMember().getId())
                .writerName(comment.getMember().getUsername())
                .writerProfile(comment.getMember().getProfile())
                .isMine(isMine)
                .build();
    }
}
