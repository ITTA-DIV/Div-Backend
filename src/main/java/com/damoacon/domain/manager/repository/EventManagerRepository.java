package com.damoacon.domain.manager.repository;

import com.damoacon.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventManagerRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT * FROM events e WHERE e.is_permit = 0", nativeQuery = true)
    List<Event> findNotPermit();
}
