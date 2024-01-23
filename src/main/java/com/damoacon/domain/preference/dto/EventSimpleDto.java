package com.damoacon.domain.preference.dto;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.event.entity.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class EventSimpleDto {
    private Long id;
    private String title;
    private Timestamp startDate;
    private String price;
    private String location;
    private String host;
    private String hostProfile;
    private String type;

    private String thumbnail;

    private String category;

    private int remainDate;

    public static EventSimpleDto fromEntity(Event event) {
        EventSimpleDto dto = new EventSimpleDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setStartDate(event.getStart_date());
        dto.setPrice(event.getPrice());
        dto.setLocation(event.getLocation());
        dto.setHost(event.getHost());
        dto.setHostProfile(event.getHostProfile());
        dto.setType(event.getType());
        dto.setThumbnail(event.getThumbnail());
        dto.setCategory(event.getCategory().getCategory_name());
        return dto;
    }



}


