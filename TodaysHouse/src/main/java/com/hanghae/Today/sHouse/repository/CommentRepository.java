package com.hanghae.Today.sHouse.repository;

import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
     Comment findTop1ByPostIdOrderByCreatedAtDesc(Long postId);
     Page<Comment> findAllByPostId(Long postId, Pageable pageable);

     List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId);

     @Query("select c.commentHeartStatus from Comment c join c.user where c.user.id = :userId and c.id = :commentId")
     Boolean findByCommentStatus(@Param("userId") Long userId, @Param("commentId") Long commentId);
     //Optional<Comment> findByComment_commentIdAndUser(Long commentId, User user);
}
