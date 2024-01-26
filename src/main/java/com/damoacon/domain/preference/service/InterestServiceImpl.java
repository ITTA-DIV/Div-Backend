package com.damoacon.domain.preference.service;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.event.repository.CategoryRepository;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.interest.InterestDto;
import com.damoacon.domain.preference.dto.interest.InterestSimpleDto;
import com.damoacon.domain.preference.entity.Interest;
import com.damoacon.domain.preference.repository.InterestRepository;
import com.damoacon.global.constant.ErrorCode;
import com.damoacon.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService{
    private final InterestRepository interestRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public InterestSimpleDto createInterest(String category, ContextUser contextUser) {
        Map<String, Long> categoryMapping = new HashMap<>();
        categoryMapping.put("창업", 1l);
        categoryMapping.put("IT/프로그래밍", 2l);
        categoryMapping.put("라이프", 3l);
        categoryMapping.put("경제/금융", 4l);
        categoryMapping.put("경영", 5l);
        categoryMapping.put("인문/사회", 6l);
        categoryMapping.put("예술", 7l);
        categoryMapping.put("마케팅", 8l);
        categoryMapping.put("커리어", 9l);
        categoryMapping.put("과학기술", 10l);
        categoryMapping.put("디자인/영상", 11l);
        categoryMapping.put("의료/의학", 12l);
        categoryMapping.put("행사 기획", 13l);
        categoryMapping.put("관광/여행", 14l);
        categoryMapping.put("기타", 15l);
        Long categoryId = categoryMapping.get(category);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category cate = optionalCategory.get();

        Member member = contextUser.getMember();

        if (interestRepository.existsByMemberAndCategory(member, cate)) {
            throw new IllegalStateException("이미 관심분야에 포함되어있음");
        }

        InterestDto interestDto = new InterestDto();
        Interest newInterest = interestDto.toEntity(member, cate);

        Interest savedInterest = interestRepository.save(newInterest);

        return InterestSimpleDto.fromEntity(savedInterest);
    }

    @Override
    @Transactional
    public long deleteInterest(String category, ContextUser contextUser) {
        Map<String, Long> categoryMapping = new HashMap<>();
        categoryMapping.put("창업", 1l);
        categoryMapping.put("IT/프로그래밍", 2l);
        categoryMapping.put("라이프", 3l);
        categoryMapping.put("경제/금융", 4l);
        categoryMapping.put("경영", 5l);
        categoryMapping.put("인문/사회", 6l);
        categoryMapping.put("예술", 7l);
        categoryMapping.put("마케팅", 8l);
        categoryMapping.put("커리어", 9l);
        categoryMapping.put("과학기술", 10l);
        categoryMapping.put("디자인/영상", 11l);
        categoryMapping.put("의료/의학", 12l);
        categoryMapping.put("행사 기획", 13l);
        categoryMapping.put("관광/여행", 14l);
        categoryMapping.put("기타", 15l);
        Long categoryId = categoryMapping.get(category);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category cate = optionalCategory.get();

        Member member = contextUser.getMember();

        Interest existingInterest = interestRepository.findByMemberAndCategory(member, cate).orElseThrow(() -> new GeneralException(ErrorCode.INTEREST_NOT_FOUND));

        interestRepository.delete(existingInterest);

        return categoryId;
    }
}
