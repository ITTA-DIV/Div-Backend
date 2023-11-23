package com.damoacon.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="events")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "date")
    private String date;

    @Column(name = "price")
    private String price;

    @Column(name = "location")
    private String location;

    @Column(name = "address")
    private String address;

    @Column(name = "host")
    private String host;

    @Column(name = "link")
    private String link;

    @Column(name = "applydate")
    private String applydate;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category cate;

    @Builder
    public Event (String title,String date,String price,String location,String address,String host,String link){
        this.title=title;
        this.date=date;
        this.price=price;
    }
}
