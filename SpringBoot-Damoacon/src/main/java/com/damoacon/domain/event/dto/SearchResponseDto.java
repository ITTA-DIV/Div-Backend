package com.damoacon.domain.event.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDto {
    private Long id;
    private String title;
    private String thumbnail;
    private String host;
    private String hostProfile;
    private String category_name;
    private String eventDateTimeString;
    private Integer remainingDays;
    private Integer isFree;
    private String location;
    private String address;
    private String type;

}
