package com.damoacon.app.survey.controller;

import com.damoacon.app.survey.dto.ResponseDto;
import com.damoacon.app.survey.dto.SurveyCreateDto;
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

    @PostMapping("new")
    public ResponseEntity<ResponseDto<Object>> save(@RequestBody SurveyCreateDto requestDto) throws Exception {
        try {
            SurveyCreateDto newSurvey = surveyCreateService.surveyCreate(requestDto);
//            System.out.println("진행중");
//            System.out.println(newSurvey.getQuestion());
            return ResponseEntity.ok().body(ResponseDto.response(HttpStatus.CREATED, "설문조사 생성 성공",newSurvey));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDto.response(HttpStatus.BAD_REQUEST, "설문조사 생성 실패"));
        }
    }

}
