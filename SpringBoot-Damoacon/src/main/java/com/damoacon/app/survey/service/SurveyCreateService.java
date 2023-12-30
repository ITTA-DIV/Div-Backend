package com.damoacon.app.survey.service;

import com.damoacon.app.event.entity.Category;
import com.damoacon.app.event.entity.Event;
import com.damoacon.app.survey.dto.SurveyCreateDto;
import com.damoacon.app.survey.entity.CategoryRepository;
import com.damoacon.app.survey.entity.EventRepository;
import com.damoacon.app.survey.entity.Survey;
import com.damoacon.app.survey.entity.SurveyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;


@RequiredArgsConstructor
@Service
public class SurveyCreateService {
    private final SurveyRepository surveyRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public SurveyCreateDto surveyCreate(SurveyCreateDto requestDto) throws Exception {
        try {
            long event_id= requestDto.getEvent_id();
            Optional <Event> eventOptional = Optional.ofNullable(eventRepository.findById(event_id));
            Event event = eventOptional.orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + event_id));
            System.out.println(event.getTitle());
            Survey savedSurvey = surveyRepository.save(requestDto.toEntity(event));
            Long new_id = savedSurvey.getId();
            Survey createdSurvey = surveyRepository.findById(new_id).orElseThrow();
            return SurveyCreateDto.fromEntity(createdSurvey);
        } catch (Exception e) {
            throw new IllegalArgumentException("설문 생성 실패");
        }

    }

}
