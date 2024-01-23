package com.damoacon.domain.model;

import com.damoacon.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class ContextUser extends User {
    private final Member member;

    public ContextUser(Member member) {
        super(member.getEmail(), member.getUsername(), Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())));
        this.member = member;
    }
}
