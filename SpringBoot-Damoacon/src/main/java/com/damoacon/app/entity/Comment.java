package com.damoacon.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @JoinColumn(name="event_id")
    @NotNull
    private Event event;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    @Column(nullable = true, name = "content")
    private String content;

    @Builder
    public Comment (Event event,User user,String content){
        this.event=event;
        this.user=user;
        this.content=content;
    }

    public void update (String content){
        this.content=content;
    }
}
