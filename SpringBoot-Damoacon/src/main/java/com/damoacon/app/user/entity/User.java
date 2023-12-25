package com.damoacon.app.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(nullable = false, name = "user_id")
    private Long id;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "profile")
    private String profile;

    @Builder
    public User (String email,String username,String profile){
        this.email=email;
        this.username=username;
        this.profile=profile;
    }
}
