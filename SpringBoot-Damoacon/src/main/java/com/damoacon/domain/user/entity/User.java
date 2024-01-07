package com.damoacon.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="User")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    private Long id;

    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "profile")
    private String profile;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = true, name = "nickname")
    private String nickname;

    @Column(nullable = false, name = "isManager")
    private Integer isManager;

    @Builder
    public User (String username,String profile, String email, String nickname, Integer isManager) {
        this.username = username;
        this.profile = profile;
        this.email = email;
        this.nickname = nickname;
        this.isManager = isManager;
    }
}
