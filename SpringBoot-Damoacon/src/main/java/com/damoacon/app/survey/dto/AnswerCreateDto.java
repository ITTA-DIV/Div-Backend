package com.damoacon.app.survey.dto;

import com.damoacon.app.survey.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateDto {
    private List<AnswerDto> surveys;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AnswerDto {
        private Long survey_id;
        private int answer;
    }
}
