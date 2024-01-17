package com.damoacon.domain.preference.controller;

import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.member.repository.MemberRepository;
import com.damoacon.domain.model.TokenClaimVo;
import com.damoacon.domain.preference.dto.comment.CommentRequestDto;
import com.damoacon.global.constant.ErrorCode;
import com.damoacon.global.exception.GeneralException;
import com.damoacon.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("postComment() Test")
    public void postComment() throws Exception {
        Member member = memberRepository.findById(1L).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

        // 댓글을 작성하려는 멤버의 액세스 토큰을 생성
        TokenClaimVo vo = new TokenClaimVo(member.getId(), member.getEmail());
        String accessToken = jwtUtil.generateToken(true, vo);

        // CommentRequestDto 생성
        CommentRequestDto requestDto = CommentRequestDto.builder()
                .content("이 이벤트 재밌었어요..ㅎ")
                .build();

        // mockMvc 요청 구성
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/comment/{id}", 118L)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto));  // 위에서 생성한 액세스 토큰을 헤더에 추가

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print());
    }
}