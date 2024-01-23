package com.damoacon.domain.preference.dto;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberSimpleDto {
    private Long id;
    private String username;
    private String nickname;
    private String profile;
    private List<Category> interest;

    public MemberSimpleDto(Member member, List<Category> categories) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.profile = member.getProfile();
        this.interest = categories;
    }


}