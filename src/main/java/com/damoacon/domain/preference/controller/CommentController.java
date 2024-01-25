package com.damoacon.domain.preference.controller;

import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.comment.CommentRequestDto;
import com.damoacon.domain.preference.service.CommentService;
import com.damoacon.global.common.ApiDataResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{eventId}")
    public ApiDataResponseDto postComment(@PathVariable Long eventId, @AuthenticationPrincipal ContextUser contextUser,
                                          @NotNull @RequestBody CommentRequestDto commentRequestDto) {

        return ApiDataResponseDto.of(commentService.postComment(eventId, contextUser.getMember(), commentRequestDto));
    }

    @DeleteMapping("/{commentId}")
    public ApiDataResponseDto deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal ContextUser contextUser) {

        return ApiDataResponseDto.of(commentService.deleteComment(commentId, contextUser.getMember()));
    }
}