package com.damoacon.domain.member.service;

import com.damoacon.domain.member.dto.MemberResponseDto;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.member.dto.GoogleLoginResponse;
import com.damoacon.domain.member.dto.GoogleUserInformation;
import com.damoacon.domain.member.repository.MemberRepository;
import com.damoacon.global.constant.ErrorCode;
import com.damoacon.global.exception.GeneralException;
import com.damoacon.global.util.JwtUtil;
import com.damoacon.global.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    @Value(value = "${google.client.id}")
    private String GOOGLE_CLIENT_ID;

    @Value(value = "${google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value(value = "${google.redirect.uri}")
    private String GOOGLE_REDIRECT_URI;

    @Value(value = "${google.auth.url}")
    private String GOOGLE_TOKEN_BASE_URL;

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final ResponseUtil responseUtil;

    @Override
    @Transactional
    public void checkIsUserAndRegister(HttpServletResponse response, GoogleUserInformation googleUserInformation) throws IOException {
        Member requestMember = Member.builder()
                .email(googleUserInformation.getEmail())
                .username(googleUserInformation.getName())
                .profile(googleUserInformation.getPicture())
                .build();

        Optional<Member> existingMemberOptional = memberRepository.findOneByEmail(requestMember.getEmail());

        if (existingMemberOptional.isPresent()) {
            requestMember = existingMemberOptional.get();
        } else {
            requestMember = memberRepository.save(requestMember);
        }

        responseUtil.setDataResponse(response, HttpServletResponse.SC_CREATED, jwtUtil.generateTokens(requestMember));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .profile(member.getProfile())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .build();

            return memberResponseDto;
        } else {
            throw new IllegalArgumentException("해당 id에 해당하는 멤버가 없습니다. id = " + id);
        }
    }

    // id_token을 통해 구글에서 유저정보를 요청해 반환 받는 메소드
    @Override
    @Transactional
    public GoogleUserInformation getUserInformation(HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        String code = request.getHeader("code");

        if(code == null)
            throw new GeneralException(ErrorCode.CODE_REQUIRED);

        try {
            GoogleLoginResponse response = requestAccessToken(code);
            String requestUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_TOKEN_BASE_URL + "/tokeninfo").queryParam("id_token", response.getId_token()).toUriString();

            return restTemplate.getForObject(requestUrl, GoogleUserInformation.class);
        } catch (SecurityException e) {
            throw new GeneralException(ErrorCode.INVALID_CODE);
        }
    }

    /**
     *
     * @param code Google API Server 에서 받아온 code
     * @return 를 바탕으로 유저정보를 요청할 수 있는 GoogleLoginResponse를 반환
     */
    @Override
    public GoogleLoginResponse requestAccessToken(String code) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> params = new HashMap<>();
            params.put("code", code);
            params.put("client_id", GOOGLE_CLIENT_ID);
            params.put("client_secret", GOOGLE_CLIENT_SECRET);
            params.put("redirect_uri", GOOGLE_REDIRECT_URI);
            params.put("grant_type", "authorization_code");

            GoogleLoginResponse googleLoginResponse =
                    restTemplate.postForEntity(GOOGLE_TOKEN_BASE_URL + "/token", params, GoogleLoginResponse.class).getBody();

            return googleLoginResponse;
        } catch (Exception e) {
            throw new IllegalArgumentException("알 수 없는 구글 로그인 Access Token 요청 URL 입니다 :: " + GOOGLE_TOKEN_BASE_URL);
        }
    }
}
