package com.damoacon.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @Column(nullable = false, name = "event")
    private Event event;

    @OneToOne
    @Column(nullable = false, name = "user")
    private User user;

    @Column(nullable = true, name = "content")
    private String content;

    public Comment (Event event,User user,String content){
        this.event=event;
        this.user=user;
        this.content=content;
    }
    public void update (String content){
        this.content=content;
    }


}
