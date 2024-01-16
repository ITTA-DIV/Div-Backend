package com.damoacon.domain.manager.controller;

import com.damoacon.domain.manager.dto.EventCreateDto;
import com.damoacon.domain.manager.dto.EventResponseDto;
import com.damoacon.domain.manager.service.ManagerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.damoacon.global.common.ApiDataResponseDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/manager")
public class ManagerController {
    private final ManagerServiceImpl managerService;

    @PostMapping
    public ApiDataResponseDto<EventResponseDto> save(@RequestBody EventCreateDto requestDto){
        try{
            EventResponseDto newEvents = managerService.eventCreate(requestDto);
            return ApiDataResponseDto.of(newEvents);
        } catch (Exception e) {
            return ApiDataResponseDto.of(null, "이벤트 신청 생성 실패");
        }
    }

    @GetMapping
    public ApiDataResponseDto<List<EventResponseDto>> applyList(){
        try {
            List<EventResponseDto> applyeventlist=managerService.eventApplyList();
            return ApiDataResponseDto.of(applyeventlist);
        } catch (Exception e) {
            return ApiDataResponseDto.of(null, "이벤트 신청 목록 조회 실패");
        }
    }

//    @PatchMapping("{event_id}")
//    public ResponseEntity<ResponseDto<Object>> changePermit(@PathVariable Long event_id){
//        try {
//            return ResponseEntity.ok().body(ResponseDto.response(HttpStatus.OK, "이벤트 요청 승인",managerService.eventApplyPermit(event_id)));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(ResponseDto.response(HttpStatus.BAD_REQUEST, "이벤트 요청 승인 실패"));
//        }
//    }
    @PatchMapping("{event_id}")
    public ApiDataResponseDto<EventResponseDto> changePermit(@PathVariable Long event_id){
        try {
            EventResponseDto changeApplyEvent = managerService.eventApplyPermit(event_id);
            return ApiDataResponseDto.of(changeApplyEvent);
        } catch (Exception e) {
            return ApiDataResponseDto.of(null, "이벤트 요청 승인 실패");
        }
    }
}
