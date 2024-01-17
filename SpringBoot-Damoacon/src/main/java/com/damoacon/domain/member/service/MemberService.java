package com.damoacon.domain.member.service;

import com.damoacon.domain.member.dto.GoogleLoginResponse;
import com.damoacon.domain.member.dto.GoogleUserInformation;
import com.damoacon.domain.member.dto.MemberResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

public interface MemberService {
    /**
     * Google API Server로부터 받은 code를 활용하여 사용자 인증 정보 요청
     * @param code Google API Server 에서 받아온 code
     * @return Google API 서버로 부터 응답받은 Json 형태의 결과를 string으로 반
     */
    GoogleLoginResponse requestAccessToken(String code);

    /**
     *
     * @param request id-token을 헤더에서 가져온다.
     * @return  id-token을 통해 구글 API 서버에서 반환 받은 유저 정보
     */
    GoogleUserInformation getUserInformation(HttpServletRequest request);

    /**
     * @param googleUserInformation Google API Server에서 받아온 User 정보를 파라미터로 받아 이미 있는 유저인지 확인하고,
     * @return 없으면 새로운 유저 생성하여 유저정보 반환, 있으면 생성하지 않고 유저정보 반환
     */
    void checkIsUserAndRegister(HttpServletResponse response, GoogleUserInformation googleUserInformation) throws IOException;

    MemberResponseDto getMember(@PathVariable Long id);
}
