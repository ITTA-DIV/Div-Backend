package com.damoacon.global.handler;

import com.damoacon.global.constant.ErrorCode;
import com.damoacon.global.exception.GeneralException;
import com.damoacon.global.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ResponseUtil responseUtil;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            responseUtil.setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.EXPIRED_JWT.getMessage());
        } catch (JwtException | IllegalArgumentException e) {   // IllegalArgumentException: 메서드에 불법적이거나 부적절한 인수가 전달되었음을 나타내기 위한 Exception
            log.error(e.getMessage());
            e.printStackTrace();
            responseUtil.setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.INVALID_TOKEN.getMessage());
        } catch (GeneralException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            responseUtil.setResponse(response, e.getErrorCode().getHttpStatus().value(), e.getErrorCode().getMessage());
        } catch (AuthenticationServiceException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            responseUtil.setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();

            responseUtil.setResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }
}