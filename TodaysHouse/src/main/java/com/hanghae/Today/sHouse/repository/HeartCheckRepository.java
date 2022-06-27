package com.hanghae.Today.sHouse.repository;

import com.hanghae.Today.sHouse.model.Heart;
import com.hanghae.Today.sHouse.model.HeartCheck;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartCheckRepository extends JpaRepository<HeartCheck, Long> {
    Optional<HeartCheck> findByPostAndUser(Post post, User user);
}
