package com.damoacon.app.event.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="category")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false,name = "category_name")
    private String category_name;

    @Builder
    public Category(String category_name){
        this.category_name=category_name;
    }
}
