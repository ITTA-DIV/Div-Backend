package com.damoacon.domain.member.service;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.member.dto.MemberResponseDto;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.member.dto.GoogleLoginResponse;
import com.damoacon.domain.member.dto.GoogleUserInformation;
import com.damoacon.domain.member.repository.MemberRepository;
import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.heart.EventSimpleDto;
import com.damoacon.domain.preference.dto.heart.MemberSimpleDto;
import com.damoacon.domain.preference.dto.interest.MyPageDto;
import com.damoacon.domain.preference.entity.Heart;
import com.damoacon.domain.preference.entity.Interest;
import com.damoacon.domain.preference.repository.HeartRepository;
import com.damoacon.domain.preference.repository.InterestRepository;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final InterestRepository interestRepository;
    private final HeartRepository heartRepository;
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

        Member member = memberRepository.findOneByEmail(requestMember.getEmail())
                .orElseGet(() -> memberRepository.save(requestMember));

        responseUtil.setDataResponse(response, HttpServletResponse.SC_CREATED, jwtUtil.generateTokens(member));
    }

    // id_token을 통해 구글에서 유저정보를 요청해 반환
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

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Member member) {

        return MemberResponseDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .profile(member.getProfile())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MyPageDto myPage(ContextUser contextUser) {
        Member member = contextUser.getMember();

        // 회원 정보
        List<Interest> interests = interestRepository.findAllByMember(member);
        List<Category> categories = interests.stream()
                .map(Interest::getCategory)
                .collect(Collectors.toList());

        MemberSimpleDto memberSimpleDto = new MemberSimpleDto(member, categories);

        // 좋아요한 이벤트 개수
        int heartCount = heartRepository.countByMember(member);

        // 좋아요한 이벤트
        List<Heart> hearts = heartRepository.findByMemberOrderByIdDesc(member);
        List<Event> heartevents = hearts.stream()
                .map(Heart::getEvent)
                .collect(Collectors.toList());
        List<EventSimpleDto> hearteventsdto = heartevents.stream()
                .map(EventSimpleDto::fromEntity)
                .collect(Collectors.toList());

        return new MyPageDto(memberSimpleDto, heartCount, hearteventsdto);
    }


    // Google API Server 에서 받아온 code를 통해 구글에 토큰 요청
    private GoogleLoginResponse requestAccessToken(String code) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> params = new HashMap<>();
            params.put("code", code);
            params.put("client_id", GOOGLE_CLIENT_ID);
            params.put("client_secret", GOOGLE_CLIENT_SECRET);
            params.put("redirect_uri", GOOGLE_REDIRECT_URI);
            params.put("grant_type", "authorization_code");

            return restTemplate.postForEntity(GOOGLE_TOKEN_BASE_URL + "/token", params, GoogleLoginResponse.class).getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("알 수 없는 구글 로그인 Access Token 요청 URL 입니다: " + GOOGLE_TOKEN_BASE_URL);
        }
    }
}
