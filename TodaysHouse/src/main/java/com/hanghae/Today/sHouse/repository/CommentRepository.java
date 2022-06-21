package com.hanghae.Today.sHouse.repository;

import com.hanghae.Today.sHouse.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
     List<Comment> findTop1ByOrderByCreatedAtDesc();
}
