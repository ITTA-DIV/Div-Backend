package com.damoacon.app.survey.service;

import com.damoacon.app.event.entity.Event;
import com.damoacon.app.survey.dto.AnswerCreateDto;
import com.damoacon.app.survey.entity.Answer;
import com.damoacon.app.survey.entity.AnswerRepository;
import com.damoacon.app.survey.entity.SurveyRepository;
import com.damoacon.app.survey.entity.Survey;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerCreateService{
    private final AnswerRepository answerRepository;
    private final SurveyRepository surveyRepository;
    @Autowired
    public AnswerCreateService(AnswerRepository answerRepository, SurveyRepository surveyRepository) {
        this.answerRepository = answerRepository;
        this.surveyRepository = surveyRepository;
    }


    public void answerCreate(List<AnswerCreateDto.AnswerDto> requestDto) throws Exception {
            try {
                for (AnswerCreateDto.AnswerDto answerDto : requestDto) {
                    Long surveyId = answerDto.getSurvey_id();
                    int answerValue = answerDto.getAnswer();
                    System.out.println(surveyId);
                    System.out.println(answerValue);

                    Survey survey = surveyRepository.findById(surveyId)
                            .orElseThrow(() -> new IllegalArgumentException("설문조사가 존재하지 않습니: " + surveyId));

                    // Create and save Answer
                    Answer answer = Answer.builder()
                            .answer(answerValue)
                            .survey(survey)
                            .build();

                    answerRepository.save(answer);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("설문 조사 실패");
            }

    }

}

