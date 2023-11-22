package com.damoacon.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="survey")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Survey {
    @Id
    @Column(nullable = false, name = "survey_id")
    private Long id;

    @Column(nullable = false, name = "question")
    private String question;

    @Column(nullable = true, name = "answer")
    private String answer;

    @OneToOne
    @Column(nullable = false, name = "event")
    private Event event;

    public Survey (String question,String answer,Event event){
        this.question=question;
        this.answer=answer;
        this.event=event;
    }
    public void update (String question,String answer){
        this.question=question;
        this.answer=answer;
    }
}
