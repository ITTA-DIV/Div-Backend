package com.damoacon.domain.preference.dto.heart;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.preference.entity.Heart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeartDto {
    private Member member;
    private Event event;

    public Heart toEntity(Member member,Event event) {
        return Heart.builder()
                .member(member)
                .event(event)
                .build();
    }

}
