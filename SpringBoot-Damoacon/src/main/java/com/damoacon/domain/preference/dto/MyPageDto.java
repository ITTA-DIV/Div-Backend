package com.damoacon.domain.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
