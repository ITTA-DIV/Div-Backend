package com.damoacon.domain.preference.dto.interest;

import com.damoacon.domain.preference.entity.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterestSimpleDto {
    private long memberId;
    private String category;

    public static InterestSimpleDto fromEntity(Interest interest) {
        InterestSimpleDto interestSimpleDto = new InterestSimpleDto();
        interestSimpleDto.setMemberId(interest.getMember().getId());
        interestSimpleDto.setCategory(interest.getCategory().getCategory_name());
        return interestSimpleDto;
    }
}
