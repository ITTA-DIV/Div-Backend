package com.damoacon.domain.manager.dto;

import com.damoacon.domain.event.entity.Category;
import com.damoacon.domain.event.entity.Event;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDto {
    @NotBlank(message = "There is no title")
    private String title;

    @NotBlank(message = "There is no startDate")
    private Timestamp startDate;

    @NotBlank(message = "There is no end_date")
    private Timestamp end_date;

    @NotBlank(message = "There is no price")
    private String price;

    @NotBlank(message = "There is no location")
    private String location;

    private String address;

    @NotBlank(message = "There is no host")
    private String host;

    private String hostProfile;

    @NotBlank(message = "There is no link")
    private String link;

    private Timestamp applyStartDate;

    private Timestamp applyEndDate;

    @NotBlank(message = "There is no type")
    private String type;


    private String thumbnail;

    private int is_permit;

    @NotBlank(message = "There is no category")
    private String category;

    private Long heartCount = 0L;

    public Event toEntity(Category category) {
        int is_permit=0;
        return Event.builder()
                .title(title)
                .startDate(startDate)
                .endDate(end_date)
                .price(price)
                .location(location)
                .address(address)
                .host(host)
                .hostProfile(hostProfile)
                .link(link)
                .applyStartDate(applyStartDate)
                .applyEndDate(applyEndDate)
                .type(type)
                .thumbnail(thumbnail)
                .isPermit(is_permit)
                .category(category)
                .build();
    }
}
