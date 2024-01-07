package com.damoacon.domain.event.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainEventResponseDto {
    private Long id;
    private String title;
    private String thumbnail;
    private String host;
    private String hostProfile;
    private String category_name;
    private String eventDateTimeString;
    private Integer remainingDays;
    private Integer isFree;
}
