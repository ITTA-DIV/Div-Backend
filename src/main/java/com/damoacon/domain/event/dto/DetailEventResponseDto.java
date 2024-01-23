package com.damoacon.domain.event.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailEventResponseDto {
    private Long id;
    private String title;
    private String thumbnail;
    private Long categoryId;
    private String categoryName;
    private String price;
    private String link;
    private String location;
    private String address;
    private String host;
    private String hostProfile;
    private String eventApplyStartDate;
    private String eventApplyEndDate;
    private String eventStartDate;
    private String eventEndDate;
}
