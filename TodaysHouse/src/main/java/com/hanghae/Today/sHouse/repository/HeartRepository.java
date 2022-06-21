package com.hanghae.Today.sHouse.repository;


import com.hanghae.Today.sHouse.dto.CommentHeartDto;
import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.Heart;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByPostAndUser(Post post, User user);
    Optional<Heart> findByCommentAndUser(Comment comment, User user);
}
