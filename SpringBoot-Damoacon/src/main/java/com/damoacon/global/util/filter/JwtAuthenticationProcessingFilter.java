package com.damoacon.global.util.filter;

import com.damoacon.domain.member.dto.LoginResponseDto;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.model.ContextUser;
import com.damoacon.global.exception.GeneralException;
import com.damoacon.global.util.JwtUtil;
import com.damoacon.global.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ResponseUtil responseUtil;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws GeneralException, ServletException, IOException, ExpiredJwtException {
        final String LOGIN_API_URL = "/api/v1/member/login/oauth/google";
        final String MAIN_EVENT_URL = "/api/v1/event";
        final String TOKEN_REFRESH_API_URL = "/api/v1/member/refresh";
        final String RESOURCE_URL = "/favicon.ico";

        String uri = request.getRequestURI();
        System.out.println(uri);
        if(uri.equals(LOGIN_API_URL) || uri.equals(MAIN_EVENT_URL) || uri.equals(RESOURCE_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        // uri가 refresh token 재발급 요청 uri이고, refresh_token이 null이 아닌 경우
        if(request.getRequestURI().equals(TOKEN_REFRESH_API_URL)) {
            String refresh_token = jwtUtil.decodeHeader(false, request);
            Member member = jwtUtil.validateToken(refresh_token);
            LoginResponseDto dto = jwtUtil.generateTokens(member);

            responseUtil.setDataResponse(response, HttpServletResponse.SC_CREATED, dto);

            return;
        }

        // access token 검증
        String access_token = jwtUtil.decodeHeader(true, request);
        Member member = jwtUtil.validateToken(access_token);

        saveAuthentication(member);
        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(Member member) {
        ContextUser contextUser = new ContextUser(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                contextUser,
                contextUser.getPassword(),
                authoritiesMapper.mapAuthorities(contextUser.getAuthorities()));

        // context 초기화 후 인증 정보 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}