package com.damoacon.app.event.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name="event")
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

    @Column(name = "start_date")
    @NotNull
    private Timestamp startDate;

    @Column(name = "end_date")
    @NotNull
    private Timestamp end_date;

    @Column(name = "price")
    @NotNull
    private String price;

    @Column(name = "location")
    @NotNull
    private String location;

    @Column(name = "address")
    private String address;

    @Column(name = "host")
    @NotNull
    private String host;

    @Column(name = "host_profile")
    @NotNull
    private String hostProfile;

    @Column(name = "link")
    @NotNull
    private String link;

    @Column(name = "apply_start_date")
    @NotNull
    private Timestamp applyStartDate;

    @Column(name = "apply_end_date")
    @NotNull
    private Timestamp applyEndDate;

    @Column(name = "type")
    @NotNull
    private String type;

    @Column(name = "thumbnail")
    @NotNull
    private String thumbnail;

    @Column(name = "is_permit")
    @NotNull
    private int is_permit;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Event(String title, Timestamp startDate, Timestamp endDate, String price, String location, String address, String host, String hostProfile, String link, Timestamp applyStartDate, Timestamp applyEndDate, String type, String thumbnail, int isPermit, Category category) {
        this.title = title;
        this.startDate = startDate;
        this.end_date = endDate;
        this.price = price;
        this.location = location;
        this.address = address;
        this.host = host;
        this.hostProfile = hostProfile;
        this.link = link;
        this.applyStartDate = applyStartDate;
        this.applyEndDate = applyEndDate;
        this.type = type;
        this.thumbnail = thumbnail;
        this.is_permit = isPermit;
        this.category = category;
    }
}
