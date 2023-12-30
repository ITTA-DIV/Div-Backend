package com.damoacon.app.survey.entity;

import com.damoacon.app.event.entity.Event;
import com.damoacon.app.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey,Long> {

    List<Survey> findAll();

    Survey findById(long id);

    List<Survey> findByEvent (Optional<Event> event);
}
