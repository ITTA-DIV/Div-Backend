package com.damoacon.domain.oauth.service;

import com.damoacon.domain.oauth.dto.GoogleLoginResponse;
import com.damoacon.domain.oauth.dto.GoogleUserInformation;
import com.damoacon.domain.oauth.dto.GoogleUserResponse;
import com.damoacon.domain.user.entity.User;
import com.damoacon.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {
    @Value(value = "${google.login.url}")
    private String GOOGLE_BASE_URL;

    @Value(value = "${google.client.id}")
    private String GOOGLE_CLIENT_ID;

    @Value(value = "${google.redirect.uri}")
    private String GOOGLE_REDIRECT_URI;

    @Value(value = "${google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value(value = "${google.auth.url}")
    private String GOOGLE_TOKEN_BASE_URL;

    @Value("${google.auth.scope}")
    private String scopes;

    private final HttpServletResponse response;

    private final UserRepository userRepository;

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

    @Override
    public void getOAuthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", getScopeUrl());
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_REDIRECT_URI);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        String redirectURL = GOOGLE_BASE_URL + "?" + parameterString;

        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GoogleUserInformation getUserInformation(String id_token) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_TOKEN_BASE_URL + "/tokeninfo").queryParam("id_token", id_token).toUriString();

        GoogleUserInformation googleUserInformation = restTemplate.getForObject(requestUrl, GoogleUserInformation.class);

        System.out.println(googleUserInformation.toString());

        return googleUserInformation;
    }

    @Override
    public GoogleUserResponse checkIsUserAndRegister(GoogleUserInformation googleUserInformation) {
        GoogleUserResponse googleUserResponse = GoogleUserResponse.builder()
                .email(googleUserInformation.getEmail())
                .name(googleUserInformation.getName())
                .picture(googleUserInformation.getPicture())
                .build();

        // User에 email 정보가 이미 존재하면, 그냥 GoogleUserResponse 반환
        if(userRepository.findOneByEmail(googleUserInformation.getEmail()).isPresent()) {
            return googleUserResponse;
        } else {    // User에 email 정보가 없으면 새로운 유저 저장
            User user = User.builder()
                    .email(googleUserResponse.getEmail())
                    .username(googleUserResponse.getName())
                    .profile(googleUserResponse.getPicture())
                    .build();

            userRepository.save(user);

            return googleUserResponse;
        }
    }

    // scope의 값을 보내기 위해 띄어쓰기 값을 UTF-8로 변환하는 로직 포함
    public String getScopeUrl() {
        return scopes.replaceAll(",", "%20");
    }
}
