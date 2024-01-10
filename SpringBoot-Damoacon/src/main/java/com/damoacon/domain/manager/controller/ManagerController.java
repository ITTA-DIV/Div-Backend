package com.damoacon.domain.manager.controller;

import com.damoacon.domain.manager.dto.EventCreateDto;
import com.damoacon.domain.manager.dto.EventResponseDto;
import com.damoacon.domain.manager.dto.ResponseDto;
import com.damoacon.domain.manager.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/manager")
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<ResponseDto<Object>> save(@RequestBody EventCreateDto requestDto){
        try {
            EventResponseDto newEvent = managerService.eventCreate(requestDto);
            return ResponseEntity.ok().body(ResponseDto.response(HttpStatus.CREATED, "이벤트 신청 생성 성공",newEvent));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDto.response(HttpStatus.BAD_REQUEST, "이벤트 신청 생성 실패"));
        }
    }
}
