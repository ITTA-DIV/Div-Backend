package com.damoacon.domain.preference.dto.interest;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.preference.entity.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterestDto {
    private Member member;
    private Category category;

    public Interest toEntity(Member member,Category category) {
        return Interest.builder()
                .member(member)
                .category(category)
                .build();
    }
}
