package com.damoacon.domain.preference.dto;

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
public class HeartSimpleDto {
    private long memberId;
    private long eventId;
    public static HeartSimpleDto fromEntity(Heart heart) {
        HeartSimpleDto heartSimpleDto = new HeartSimpleDto();
        heartSimpleDto.setMemberId(heart.getMember().getId());
        heartSimpleDto.setEventId(heart.getEvent().getId());
        return heartSimpleDto;
    }

}
