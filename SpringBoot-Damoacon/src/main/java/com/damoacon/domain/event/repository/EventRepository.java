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

    //keyword를 포함하는 title을 가지는 Event를 가져오는 쿼리(title 검색)
    @Query("select e from Event e where lower(e.title) like lower(concat('%', :keyword, '%'))")
    List<Event> findEventByKeyword(@Param("keyword") String keyword);

    //start_date와 end_date 사이 기간을 충족하는 Event를 가져오는 쿼리(date 검색)
    @Query("select e from Event e where e.start_date <= :start_date and e.end_date >= :end_date")
    List<Event> findEventByDate(@Param("start_date") Timestamp start_date, @Param("end_date") Timestamp end_date);

    //address 검색 쿼리
    @Query("select e from Event e where e.address like concat('%', :addr, '%')")
    List<Event> findEventsByAddress(@Param("addr") String addr);

    //price 유료인 Event 조회 쿼리
    @Query("SELECT e FROM Event e WHERE e.price = '무료'")
    List<Event> findEventsByFreePrice();

    //price 유료인 Event 조회 쿼리
    @Query("SELECT e FROM Event e WHERE e.price = '유료'")
    List<Event> findEventsByNotFreePrice();

    //maxPrice보다 낮은 price를 가진 Event를 가져오는 쿼리
    @Query("SELECT e FROM Event e WHERE CAST(REPLACE(e.price, '원', '') AS Long) <= :maxPrice")
    List<Event> findEventsByMaxPrice(@Param("maxPrice") Long maxPrice);

    //minPrice보다 높은 price를 가진 Event를 가져오는 쿼리
    @Query("SELECT e FROM Event e WHERE CAST(REPLACE(e.price, '원', '') AS Long) >= :minPrice")
    List<Event> findEventsByMinPrice(@Param("minPrice") Long minPrice);

    //minPrice보다 높고 maxPrice보다 낮은 price를 가진 Event를 가져오는 쿼리
    @Query("SELECT e FROM Event e WHERE CAST(REPLACE(e.price, '원', '') AS Long) BETWEEN :minPrice AND :maxPrice")
    List<Event> findEventsByPriceRange(@Param("minPrice") Long minPrice, @Param("maxPrice") Long maxPrice);

    //location 검색 쿼리
    @Query("select e from Event e where lower(e.location) like lower(concat('%', :location, '%'))")
    List<Event> findEventsByLocation(@Param("location") String location);

    //type 검색 쿼리
    @Query("select e from Event e where lower(e.type) = lower(:type)")
    List<Event> findEventsByType(@Param("type") String type);

    //category 검색 쿼리
    @Query("SELECT e FROM Event e JOIN e.category c WHERE c.category_name = :categoryName")
    List<Event> findEventsByCategoryName(@Param("categoryName") String categoryName);

}