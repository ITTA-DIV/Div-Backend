package com.damoacon.app.survey.entity;

import com.damoacon.app.event.entity.Event;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="survey")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "survey_id")
    private Long id;

    @Column(nullable = false, name = "question")
    private String question;

    @ManyToOne
    @MapsId("event_id")
    private Event event;

    @Builder
    public Survey (String question, Event event){
        this.question = question;
        this.event = event;
    }
    public void update (String question){
        this.question = question;
    }
}
