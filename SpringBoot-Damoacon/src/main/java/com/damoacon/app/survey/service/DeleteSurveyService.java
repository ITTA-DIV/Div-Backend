package com.damoacon.app.survey.service;

import com.damoacon.app.event.entity.Event;
import com.damoacon.app.survey.dto.SurveyCreateDto;
import com.damoacon.app.survey.entity.Survey;
import com.damoacon.app.survey.entity.SurveyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeleteSurveyService {
    private final SurveyRepository surveyRepository;
    public void surveyDelete(long survey_id) {
        try {
            surveyRepository.deleteById(survey_id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("삭제할 Survey를 찾을 수 없습니다. survey_id: " + survey_id);
        } catch (Exception e) {
            throw new IllegalArgumentException("설문 삭제 실패", e);
        }

    }
}
