package com.damoacon.app.survey.entity;

import com.damoacon.app.event.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer,Long> {

}