package com.damoacon.domain.member.service;

import com.damoacon.domain.member.dto.GoogleLoginResponse;
import com.damoacon.domain.member.dto.GoogleUserInformation;
import com.damoacon.domain.member.dto.LoginResponseDto;
import com.damoacon.global.common.ApiDataResponseDto;

public interface MemberService {
    void getOAuthRedirectURL();

    /**
     * Google API Server로부터 받은 code를 활용하여 사용자 인증 정보 요청
     * @param code Google API Server 에서 받아온 code
     * @return Google API 서버로 부터 응답받은 Json 형태의 결과를 string으로 반
     */
    GoogleLoginResponse requestAccessToken(String code);

    /**
     * @param id_token Google API Server에서 받아온 id_token으로
     * @return Google API 서버로부터 응답받은 Json 형태의 결과를 GoogleUserResponse로 반환
     */
    GoogleUserInformation getUserInformation(String id_token);

    /**
     * @param googleUserInformation Google API Server에서 받아온 User 정보를 파라미터로 받아 이미 있는 유저인지 확인하고,
     * @return 없으면 새로운 유저 생성하여 유저정보 반환, 있으면 생성하지 않고 유저정보 반환
     */
    ApiDataResponseDto<LoginResponseDto> checkIsUserAndRegister(GoogleUserInformation googleUserInformation);
}
