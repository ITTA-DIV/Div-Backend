package com.damoacon.domain.preference.repository;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.preference.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findAllByMember(Member member);

    Optional<Interest> findByMemberAndCategory(Member member, Category category);

    boolean existsByMemberAndCategory(Member member, Category category);
}
