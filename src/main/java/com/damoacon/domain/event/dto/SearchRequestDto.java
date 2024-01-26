package com.damoacon.domain.event.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDto {
    private String keywords;        //title과 매칭
    private Timestamp start_date;
    private Timestamp end_date;
    private String price;
    private String location;
    private String address;
    private String type;
    private String category_name;
    private boolean free;
    private boolean notFree;
    private Long minPrice;
    private Long maxPrice;
}
