package com.damoacon.domain.member.controller;

import com.damoacon.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value = "/login/oauth/google")
    public void login(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws IOException {

        memberService.checkIsUserAndRegister(response, memberService.getUserInformation(request));
    }
}
