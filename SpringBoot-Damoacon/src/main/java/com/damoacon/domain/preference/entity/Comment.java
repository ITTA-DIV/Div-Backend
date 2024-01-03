package com.damoacon.domain.preference.entity;

import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="comment")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, name = "content")
    private String content;

    @Builder
    public Comment (Event event, User user, String content) {
        this.event = event;
        this.user = user;
        this.content = content;
    }

    public void update (String content) {
        this.content = content;
    }
}
