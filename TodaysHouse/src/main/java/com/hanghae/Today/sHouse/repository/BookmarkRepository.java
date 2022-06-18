package com.hanghae.Today.sHouse.repository;

import com.hanghae.Today.sHouse.model.Bookmark;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByPostAndUser(Post post, User user);

}
