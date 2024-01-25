package com.damoacon.domain.member.service;

import com.damoacon.domain.member.dto.GoogleUserInformation;
import com.damoacon.domain.member.dto.MemberResponseDto;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.global.common.ApiDataResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MemberService {
    GoogleUserInformation getUserInformation(HttpServletRequest request);

    void checkIsUserAndRegister(HttpServletResponse response, GoogleUserInformation googleUserInformation) throws IOException;

    MemberResponseDto getMember(Member member);
}
