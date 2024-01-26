package com.damoacon.domain.preference.repository;

import com.damoacon.domain.preference.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.event.id = :eventId ORDER BY c.lastModifiedTime ASC LIMIT 100")
    List<Comment> findCommentsByEventId(@Param("eventId") Long eventId);
}
