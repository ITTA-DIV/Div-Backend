package com.damoacon.domain.member.controller;

import com.damoacon.domain.member.dto.GoogleUserInformation;
import com.damoacon.domain.member.dto.LoginResponseDto;
import com.damoacon.domain.member.service.MemberService;
import com.damoacon.global.common.ApiDataResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member/login/oauth")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping(value = "/google")
    public void googleLoginRedirect() {
        log.info(">> 사용자로부터 Google 로그인 요청을 받음 :: {} Google Login");

        memberService.getOAuthRedirectURL();
    }

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param code API Server 로부터 넘어노는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
     */
    @GetMapping(value = "/google/callback")
    public ApiDataResponseDto<LoginResponseDto> callback(@RequestParam(name = "code") String code) {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);
        String idToken = memberService.requestAccessToken(code).getId_token();
        GoogleUserInformation googleUserResponse = memberService.getUserInformation(idToken);

        return memberService.checkIsUserAndRegister(googleUserResponse);
    }
}
