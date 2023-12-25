package com.damoacon.app.preference.entity;

import com.damoacon.app.event.entity.Event;
import com.damoacon.app.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @JoinColumn(name="user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name="event_id")
    @NotNull
    private Event event;

    @Builder
    public Heart(User user, Event event){
        this.event=event;
        this.user=user;
    }
}
