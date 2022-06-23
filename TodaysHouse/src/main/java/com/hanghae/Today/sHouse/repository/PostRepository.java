package com.hanghae.Today.sHouse.repository;

import com.hanghae.Today.sHouse.dto.PostResponseDto;
import com.hanghae.Today.sHouse.model.Heart;
import com.hanghae.Today.sHouse.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    //Page<Post> findAll(Pageable pageable);

    //랭킹 페이지 구현
    //List<Post> findAllByOrderByViewCntDesc();
    //public List<?> findTop5ByOrderByviewCntDesc();
    @Query("select p from Post p")
    List<Post> findAllByPostRanking(Pageable pageable);

    //마이페이지 사진4장
    @Query("select p from Post p join p.user u where u.id = :userId")
    List<Post> findByMypagePicture(Pageable pageable, @Param("userId") Long userId);

    //마이페이지 게시글 보기
    @Query("select p from Post p join p.user u where u.id = :userId order by p.createdAt desc ")
    List<Post> findByMypagePosting(@Param("userId") Long userId);

    //유저가 누른 좋아요 갯수
    @Query("select count(h) from Post p join p.user u join p.heart h where h.user.id = :userId")
    int findByUserHeartCnt(@Param("userId") Long userId);

    //유저가 누른 북마크 갯수
    @Query("select count(b) from Post p join p.user u join p.bookmark b where b.user.id = :userId")
    int findByUserBookmarkCnt(@Param("userId") Long userId);
}
