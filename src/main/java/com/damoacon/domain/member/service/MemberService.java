package com.damoacon.domain.member.service;

import com.damoacon.domain.member.dto.GoogleUserInformation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MemberService {
    GoogleUserInformation getUserInformation(HttpServletRequest request);

    void checkIsUserAndRegister(HttpServletResponse response, GoogleUserInformation googleUserInformation) throws IOException;
}
