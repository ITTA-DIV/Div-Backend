package com.damoacon.domain.preference.entity;

import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="heart")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Builder
    public Heart(User user, Event event) {
        this.event = event;
        this.user = user;
    }
}
