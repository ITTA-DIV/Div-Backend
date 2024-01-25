package com.damoacon.domain.preference.service;

import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.interest.InterestSimpleDto;

public interface InterestService {
    InterestSimpleDto createInterest(String category, ContextUser contextUser);
    long deleteInterest(String category, ContextUser contextUser);
}
