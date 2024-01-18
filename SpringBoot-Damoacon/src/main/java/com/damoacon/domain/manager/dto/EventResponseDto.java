package com.damoacon.domain.manager.dto;

import com.damoacon.domain.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDto {
    private Long id;
    private String title;
    private Timestamp startDate;
    private Timestamp endDate;
    private String price;
    private String location;
    private String address;
    private String host;
    private String hostProfile;
    private String link;
    private Timestamp applyStartDate;
    private Timestamp applyEndDate;
    private String type;
    private String thumbnail;
    private int isPermit;
    private String category;
    private Long heartCount;

    public static EventResponseDto fromEntity(Event event) {
        EventResponseDto dto = new EventResponseDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setStartDate(event.getStart_date());
        dto.setEndDate(event.getEnd_date());
        dto.setPrice(event.getPrice());
        dto.setLocation(event.getLocation());
        dto.setAddress(event.getAddress());
        dto.setHost(event.getHost());
        dto.setHostProfile(event.getHostProfile());
        dto.setLink(event.getLink());
        dto.setApplyStartDate(event.getApplyStartDate());
        dto.setApplyEndDate(event.getApplyEndDate());
        dto.setType(event.getType());
        dto.setThumbnail(event.getThumbnail());
        dto.setIsPermit(event.getIs_permit());
        dto.setCategory(event.getCategory().getCategory_name());  // Assuming Category has a 'name' field
        dto.setHeartCount(event.getHeartCount());
        return dto;
    }
}
