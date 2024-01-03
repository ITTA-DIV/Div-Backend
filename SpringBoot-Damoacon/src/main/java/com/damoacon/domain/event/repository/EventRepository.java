package com.damoacon.domain.event.repository;

import com.damoacon.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // 신청 기간이 8일 이하로 남은 이벤트 12개를 불러오는 메소드
    @Query(value = "SELECT * FROM events e WHERE e.apply_end_date BETWEEN CURRENT_TIMESTAMP AND :eightDaysAfter LIMIT 12", nativeQuery = true)
    List<Event> findEventsWithinEightDays(@Param("eightDaysAfter") Timestamp eightDaysAfter);

    // id를 기준으로 내림차순으로 정렬한 결과 최상위 12개의 이벤트를 가져오는 메서드
    @Query("SELECT e FROM Event e ORDER BY e.id DESC LIMIT 12")
    List<Event> findLast12Events();

    // HeartCount를 기준으로 내림차순하여 상위 12개 Event를 가져오는 쿼리
    @Query("SELECT e FROM Event e WHERE e.applyEndDate > CURRENT_TIMESTAMP ORDER BY e.heartCount DESC")
    List<Event> findTop12EventsByHeartCount();
}