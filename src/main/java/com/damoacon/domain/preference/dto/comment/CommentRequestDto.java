package com.damoacon.domain.preference.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    @Size(max = 500, message = "Content should not exceed 500 characters.")
    @NotBlank(message = "Content should not be blank.")
    private String content;
}