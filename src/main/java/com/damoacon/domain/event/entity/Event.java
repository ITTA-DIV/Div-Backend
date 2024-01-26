package com.damoacon.domain.event.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name="events")
@Getter
@Setter
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
    private Timestamp start_date;

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

    @Column(name = "heart_count")
    @NotNull
    private Long heartCount = 0L;

    @Builder
    public Event(String title, Timestamp startDate, Timestamp endDate, String price, String location, String address, String host, String hostProfile, String link, Timestamp applyStartDate, Timestamp applyEndDate, String type, String thumbnail, int isPermit, Category category) {
        this.title = title;
        this.start_date = startDate;
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

    // retainAll()이 작동하도록 하기 위함
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
