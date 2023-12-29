package com.damoacon.app.survey.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="answer")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "answer_id")
    private Long id;

    @Column(nullable = false, name = "answer")
    @NotNull
    private int answer;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Builder
    public Answer(int answer, Survey survey) {
        this.answer = answer;
        this.survey = survey;
    }
}
