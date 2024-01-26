package com.damoacon.domain.preference.service;

import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.heart.HeartSimpleDto;

public interface HeartService {
    HeartSimpleDto createHeart(long event_id, ContextUser contextUser);

    long deleteHeart(long event_id, ContextUser contextUser);
}