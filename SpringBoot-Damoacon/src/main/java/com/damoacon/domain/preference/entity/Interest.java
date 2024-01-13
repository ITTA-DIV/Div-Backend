package com.damoacon.domain.preference.entity;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.member.entity.Member;
import jakarta.persistence.*;
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
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Interest(Member member, Category category) {
        this.member = member;
        this.category = category;
    }
}
