package com.damoacon.domain.preference.service;

import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.event.repository.EventRepository;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.member.repository.MemberRepository;
import com.damoacon.domain.preference.dto.comment.CommentRequestDto;
import com.damoacon.domain.preference.dto.comment.CommentResponseDto;
import com.damoacon.domain.preference.entity.Comment;
import com.damoacon.domain.preference.repository.CommentRepository;
import com.damoacon.global.constant.ErrorCode;
import com.damoacon.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Long postComment(Long eventId, Member member, CommentRequestDto commentRequestDto) throws GeneralException {
        // validate Member and Event
        memberRepository.findById(member.getId()).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        Event event = validateEvent(eventId);

        Comment comment = Comment.builder()
                .event(event)
                .member(member)
                .content(commentRequestDto.getContent())
                .build();

        return commentRepository.save(comment).getId();
    }

    @Override
    @Transactional
    public Long deleteComment(Long commentId, Member member) throws GeneralException, IllegalArgumentException {
        commentRepository.delete(validateDeleteComment(commentId, member));

        return commentId;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long eventId, Member member) {
         List<Comment> comments = commentRepository.findCommentsByEventId(validateEvent(eventId).getId());

        return comments.stream()
                .map(comment -> toResponse(comment, member))
                .collect(Collectors.toList());
    }

    private Comment validateDeleteComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new GeneralException(ErrorCode.COMMENT_NOT_FOUND));
        memberRepository.findById(member.getId()).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

        if(member.getId() != comment.getMember().getId()) { // 댓글을 작성한 멤버의 id와 댓글을 삭제하려는 멤버 id가 다른 경우
            throw new IllegalArgumentException("댓글을 작성한 멤버와 삭제하려는 멤버가 일치하지 않습니다.");
        } else {
            return comment;
        }
    }

    private Event validateEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new GeneralException(ErrorCode.EVENT_NOT_FOUND));

        return event;
    }

    private CommentResponseDto toResponse(Comment comment, Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");

        boolean isMine = false;
        if (member != null) isMine = member.getId().equals(comment.getMember().getId());

        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .creationTime(comment.getLastModifiedTime().format(formatter))
                .writerId(comment.getMember().getId())
                .writerName(comment.getMember().getUsername())
                .writerProfile(comment.getMember().getProfile())
                .isMine(isMine)
                .build();
    }
}
