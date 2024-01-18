package com.damoacon.domain.preference.service;

import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.preference.dto.comment.CommentRequestDto;
import com.damoacon.domain.preference.dto.comment.CommentResponseDto;
import com.damoacon.global.exception.GeneralException;

import java.util.List;

public interface CommentService {
    Long postComment(Long eventId, Member member, CommentRequestDto commentRequestDto) throws GeneralException;

    Long deleteComment(Long commentId, Member member) throws GeneralException, IllegalArgumentException;

    List<CommentResponseDto> getComments(Long eventId, Member member);
}
