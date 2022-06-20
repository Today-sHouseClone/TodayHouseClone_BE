package com.hanghae.Today.sHouse.repository;

import com.hanghae.Today.sHouse.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByViewCntDesc();


}
