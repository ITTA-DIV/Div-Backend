package com.damoacon.domain.manager.controller;

import com.damoacon.domain.manager.dto.EventCreateDto;
import com.damoacon.domain.manager.dto.EventResponseDto;
import com.damoacon.domain.manager.dto.ManagerCheckDto;
import com.damoacon.domain.manager.service.ManagerServiceImpl;
import com.damoacon.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.damoacon.global.common.ApiDataResponseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.damoacon.domain.model.ContextUser;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/manager")
public class ManagerController {
    private final ManagerServiceImpl managerService;

    @PostMapping("create")
    public ApiDataResponseDto<EventResponseDto> save(@RequestBody EventCreateDto requestDto){
        try{
            EventResponseDto newEvents = managerService.eventCreate(requestDto);
            return ApiDataResponseDto.of(newEvents);
        } catch (Exception e) {
            throw new IllegalArgumentException("이벤트 신청 생성 실패.title이 중복되는 event는 등록할수 없습니다");
        }
    }

    @GetMapping("applylist")
    public ApiDataResponseDto<List<EventResponseDto>> applyList(){
        try {
            List<EventResponseDto> applyeventlist = managerService.eventApplyList();
            return ApiDataResponseDto.of(applyeventlist);
        } catch (Exception e) {
            throw new IllegalArgumentException("이벤트 신청 목록 조회 실패");
        }
    }

    @PatchMapping("/{event_id}")
    public ApiDataResponseDto<EventResponseDto> changePermit(@PathVariable Long event_id){
        try {
            EventResponseDto changeApplyEvent = managerService.eventApplyPermit(event_id);
            return ApiDataResponseDto.of(changeApplyEvent);
        } catch (Exception e) {
            throw new IllegalArgumentException("이벤트 요청 승인 실패");
        }
    }

    @GetMapping("/check")
    public ApiDataResponseDto<ManagerCheckDto> checkManager (@AuthenticationPrincipal ContextUser contextUser){
        try{
            Long memberId = contextUser.getMember().getId();
            String memberUserName = contextUser.getMember().getUsername();
            boolean memberIsManager = Role.ROLE_USER.equals(contextUser.getMember().getRole());

            ManagerCheckDto managerCheckDto = new ManagerCheckDto(memberId, memberUserName, memberIsManager);
            return ApiDataResponseDto.of(managerCheckDto);

        } catch (Exception e) {
            throw new IllegalArgumentException("관리자여부 조회 실패");
        }

    }
}
