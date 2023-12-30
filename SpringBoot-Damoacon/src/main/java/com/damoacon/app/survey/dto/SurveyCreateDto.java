package com.damoacon.app.survey.dto;

import com.damoacon.app.event.entity.Event;
import com.damoacon.app.survey.entity.Survey;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SurveyCreateDto {
    @NotBlank(message = "There is no question")
    private String question;

//    @NotBlank(message = "There is no event")
    private Long event_id; // Use Event directly

    public Survey toEntity(Event event) {
        return Survey.builder()
                .question(question)
                .event(event)
                .build();
    }
    public static SurveyCreateDto fromEntity(Survey survey) {
        return new SurveyCreateDto(survey.getQuestion(), survey.getEvent().getId());
    }

}
