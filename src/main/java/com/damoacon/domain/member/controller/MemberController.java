package com.damoacon.domain.member.controller;

import com.damoacon.domain.member.dto.MemberResponseDto;
import com.damoacon.domain.member.service.MemberService;
import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.member.dto.MyPageDto;
import com.damoacon.global.common.ApiDataResponseDto;
import com.damoacon.global.util.JwtUtil;
import com.damoacon.global.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final ResponseUtil responseUtil;

    @PostMapping(value = "/login/oauth/google")
    public void login(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws IOException {

        memberService.checkIsUserAndRegister(response, memberService.getUserInformation(request));
    }

    @GetMapping
    public ApiDataResponseDto<MemberResponseDto> getMember(@AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(memberService.getMember(contextUser.getMember()));
    }

    @GetMapping(value = "/refresh")
    public void getRefresh(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws IOException {

        memberService.getRefresh(request, response);
    }

    @GetMapping("/mypage")
    public ApiDataResponseDto<MyPageDto> myPage(@AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(memberService.myPage(contextUser));
    }
}
