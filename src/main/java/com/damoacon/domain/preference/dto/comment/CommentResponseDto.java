package com.damoacon.domain.preference.dto.comment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String creationTime;
    private Long writerId;
    private String writerProfile;
    private String writerName;
    private boolean isMine;
}
