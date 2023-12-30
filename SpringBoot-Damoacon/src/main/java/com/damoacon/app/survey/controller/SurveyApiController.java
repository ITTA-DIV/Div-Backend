package com.damoacon.app.survey.controller;

import com.damoacon.app.survey.dto.AnswerCreateDto;
import com.damoacon.app.survey.dto.ResponseDto;
import com.damoacon.app.survey.dto.SurveyCreateDto;
import com.damoacon.app.survey.service.AnswerCreateService;
import com.damoacon.app.survey.service.DeleteSurveyService;
import com.damoacon.app.survey.service.SurveyCreateService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("survey")
public class SurveyApiController {
    private final SurveyCreateService surveyCreateService;
    private final AnswerCreateService answerCreateService;
    private final DeleteSurveyService deleteSurveyService;

    @PostMapping("new")
    public ResponseEntity<ResponseDto<Object>> save(@RequestBody SurveyCreateDto requestDto) throws Exception {
        try {
            SurveyCreateDto newSurvey = surveyCreateService.surveyCreate(requestDto);
            return ResponseEntity.ok().body(ResponseDto.response(HttpStatus.CREATED, "설문조사 생성 성공",newSurvey));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDto.response(HttpStatus.BAD_REQUEST, "설문조사 생성 실패"));
        }
    }
    @PostMapping("answer")
    public ResponseEntity<ResponseDto<Object>> answer(@RequestBody AnswerCreateDto requestDto) throws Exception {
        try {
            answerCreateService.answerCreate(requestDto.getSurveys());
            return ResponseEntity.ok().body(ResponseDto.response(HttpStatus.CREATED, "설문조사 성공"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDto.response(HttpStatus.BAD_REQUEST, "설문조사 실패"));
        }
    }
    @DeleteMapping ("{survey_id}")
    public ResponseEntity<ResponseDto<Object>> deleteSurvey(@PathVariable("survey_id") String survey_id) throws Exception {
        try {
            long parsedSurveyId = Long.parseLong(survey_id);
            deleteSurveyService.surveyDelete(parsedSurveyId);

            return ResponseEntity.ok().body(ResponseDto.response(HttpStatus.OK, "설문조사 삭제 성공"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDto.response(HttpStatus.BAD_REQUEST, "설문조사 삭제 실패"));
        }
    }


}
