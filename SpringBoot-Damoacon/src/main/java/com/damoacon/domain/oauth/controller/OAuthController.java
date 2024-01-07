package com.damoacon.domain.oauth.controller;

import com.damoacon.domain.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/oauth2")
@Slf4j
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping(value = "/google")
    public void socialLoginType() {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login");
        oAuthService.getOAuthRedirectURL();
    }

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param code API Server 로부터 넘어노는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
     */
    @GetMapping(value = "/google/callback")
    public String callback(@RequestParam(name = "code") String code) {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);
        return oAuthService.requestAccessToken(code);
    }
}
