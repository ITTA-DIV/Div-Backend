package com.damoacon.app.survey.entity;

import com.damoacon.app.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
    Event findById(long id);

}
