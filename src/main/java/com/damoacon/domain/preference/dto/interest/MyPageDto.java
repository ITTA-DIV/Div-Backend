package com.damoacon.domain.preference.dto.interest;

import com.damoacon.domain.preference.dto.heart.EventSimpleDto;
import com.damoacon.domain.preference.dto.heart.MemberSimpleDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyPageDto {
    private MemberSimpleDto memberInfo;
    private int heartNum;
    private List<EventSimpleDto> heartEvents;

    public MyPageDto(MemberSimpleDto memberInfo,int heartNum,List<EventSimpleDto> heartEvents){
        this.memberInfo=memberInfo;
        this.heartNum=heartNum;
        this.heartEvents=heartEvents;
    }
}
