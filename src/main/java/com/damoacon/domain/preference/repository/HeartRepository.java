package com.damoacon.domain.preference.repository;

import com.damoacon.domain.event.entity.Event;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.preference.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByMemberAndEvent(Member member, Event event);

    Optional<Heart> findByMemberAndEvent(Member member, Event event);

    int countByMember(Member member);

    List<Heart> findByMemberOrderByIdDesc(Member member);
}
