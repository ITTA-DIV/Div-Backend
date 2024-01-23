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
        final String TOKEN_REFRESH_API_URL = "/api/v1/member/refresh";

        // 토큰 재발급 요청이 올 경우
        if(request.getRequestURI().equals(TOKEN_REFRESH_API_URL)) {
            // refresh token을 헤더에 가지고 있는 경우 검증 후 token 재발급
            String refreshToken = jwtUtil.decodeHeader(false, request);

            // refresh_token 만료 검증
            jwtUtil.validateToken(refreshToken);
            Member member = jwtUtil.getMember(refreshToken);
            LoginResponseDto dto = jwtUtil.generateTokens(member);

            responseUtil.setDataResponse(response, HttpServletResponse.SC_CREATED, dto);

            return;
        } else {    // 재발급 요청 이외의 모든 요청 처리
            // access token을 헤더에 가지고 있는 경우 검증
            String token = jwtUtil.decodeHeader(true, request);

            if (token != null && jwtUtil.validateToken(token)) {
                // access token 검증
                String accessToken = jwtUtil.decodeHeader(true, request);
                Member member = jwtUtil.getMember(accessToken);

                saveAuthentication(member);
                filterChain.doFilter(request, response);

                return;
            }
        }

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