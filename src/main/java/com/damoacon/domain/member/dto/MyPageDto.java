package com.damoacon.domain.member.dto;

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
