package com.damoacon.app.survey.controller;

import com.damoacon.app.survey.dto.AnswerCreateDto;
import com.damoacon.app.survey.dto.ResponseDto;
import com.damoacon.app.survey.dto.SurveyCreateDto;
import com.damoacon.app.survey.service.AnswerCreateService;
import com.damoacon.app.survey.service.SurveyCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("survey")
public class SurveyApiController {
    private final SurveyCreateService surveyCreateService;
    private final AnswerCreateService answerCreateService;

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


}
