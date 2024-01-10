package com.damoacon.domain.manager.controller;

import com.damoacon.domain.manager.dto.EventCreateDto;
import com.damoacon.domain.manager.dto.EventResponseDto;
import com.damoacon.domain.manager.dto.ResponseDto;
import com.damoacon.domain.manager.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    public ResponseEntity<ResponseDto<Object>> applyList(){
        try {
            return ResponseEntity.ok().body(ResponseDto.response(HttpStatus.OK, "이벤트 신청 목록 조회 성공",managerService.eventApplyList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDto.response(HttpStatus.BAD_REQUEST, "이벤트 신청 목록 조회 실패"));
        }
    }

    @PatchMapping("{event_id}")
    public ResponseEntity<ResponseDto<Object>> changePermit(@PathVariable Long event_id){
        try {
            return ResponseEntity.ok().body(ResponseDto.response(HttpStatus.OK, "이벤트 요청 승인",managerService.eventApplyPermit(event_id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDto.response(HttpStatus.BAD_REQUEST, "이벤트 요청 승인 실패"));
        }
    }
}
