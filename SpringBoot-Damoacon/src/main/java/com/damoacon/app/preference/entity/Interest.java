package com.damoacon.app.preference.entity;

import com.damoacon.app.event.entity.Category;
import com.damoacon.app.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="interest")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name="category_id")
    @NotNull
    private Category cate;

    @Builder
    public Interest(User user, Category cate){
        this.user=user;
        this.cate=cate;
    }
}
