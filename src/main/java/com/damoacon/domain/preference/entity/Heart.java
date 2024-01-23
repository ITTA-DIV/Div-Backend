package com.damoacon.domain.preference.entity;

import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="heart")
@Getter
@Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Builder
    public Heart(Member member, Event event) {
        this.event = event;
        this.member = member;
    }
}
