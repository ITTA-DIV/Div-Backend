package com.damoacon.domain.preference.service;

import com.damoacon.domain.model.ContextUser;
import com.damoacon.domain.preference.dto.HeartDto;
import com.damoacon.domain.preference.dto.HeartSimpleDto;
import com.damoacon.domain.preference.dto.MyPageDto;

public interface HeartService {
    HeartSimpleDto createHeart(long event_id, ContextUser contextUser);
    void deleteHeart(long event_id, ContextUser contextUser);
    MyPageDto myPage(ContextUser contextUser);
}
