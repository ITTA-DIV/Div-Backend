package com.damoacon.global.config;

import com.damoacon.global.constant.ErrorCode;
import com.damoacon.global.handler.ExceptionHandlerFilter;
import com.damoacon.global.util.JwtUtil;
import com.damoacon.global.util.ResponseUtil;
import com.damoacon.global.util.filter.JwtAuthenticationProcessingFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final ResponseUtil responseUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(httpRequests -> httpRequests
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/")
                                , new AntPathRequestMatcher("/css/**")
                                , new AntPathRequestMatcher("/images/**")
                        ).permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/api/v1/member/login/oauth/google").permitAll()
                        .requestMatchers("/api/v1/manager/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationProcessingFilter(jwtUtil, responseUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(responseUtil), JwtAuthenticationProcessingFilter.class)
                .exceptionHandling((exceptionConfig) ->
                    exceptionConfig
                    .accessDeniedHandler(new AccessDeniedHandler() {
                        @Override
                        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                            responseUtil.setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.UNAUTHORIZED_ROLE.getMessage());
                        }
                    })
                    .authenticationEntryPoint(new AuthenticationEntryPoint() {
                        @Override
                        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                            responseUtil.setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.LOGIN_REQUIRED.getMessage());
                        }
                    })
                );

        return httpSecurity.build();
    }
}