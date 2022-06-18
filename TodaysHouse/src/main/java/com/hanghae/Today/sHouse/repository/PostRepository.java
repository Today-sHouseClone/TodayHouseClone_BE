package com.hanghae.Today.sHouse.repository;

import com.hanghae.Today.sHouse.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    Page<Post> findAll(Pageable pageable);
}
