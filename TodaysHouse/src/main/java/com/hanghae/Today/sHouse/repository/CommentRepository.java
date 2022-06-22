package com.hanghae.Today.sHouse.repository;

import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
     Comment findTopByPostIdOrderByCreatedAtDesc(Long postId);

    Page<Comment> findAll(Pageable pageable);
}
