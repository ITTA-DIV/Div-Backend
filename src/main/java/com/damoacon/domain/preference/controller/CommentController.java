package com.damoacon.domain.preference.controller;

import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.comment.CommentRequestDto;
import com.damoacon.domain.preference.service.CommentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/{eventId}")
    public Long postComment(@PathVariable Long eventId, @AuthenticationPrincipal ContextUser contextUser,
                            @NotNull @RequestBody CommentRequestDto commentRequestDto) {

        return commentService.postComment(eventId, contextUser.getMember(), commentRequestDto);
    }

    @DeleteMapping("/{commentId}")
    public Long deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal ContextUser contextUser) {

        return commentService.deleteComment(commentId, contextUser.getMember());
    }
}