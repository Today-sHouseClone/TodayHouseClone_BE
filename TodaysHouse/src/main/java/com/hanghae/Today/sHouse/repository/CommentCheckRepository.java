package com.hanghae.Today.sHouse.repository;

import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.CommentCheck;
import com.hanghae.Today.sHouse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentCheckRepository extends JpaRepository<CommentCheck, Long> {
    Optional<CommentCheck> findByCommentAndUser(Comment comment, User user);
}
