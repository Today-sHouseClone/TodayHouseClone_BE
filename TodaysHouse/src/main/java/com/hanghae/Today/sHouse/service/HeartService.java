package com.hanghae.Today.sHouse.service;


import com.hanghae.Today.sHouse.dto.CommentHeartDto;
import com.hanghae.Today.sHouse.dto.PostHeartDto;
import com.hanghae.Today.sHouse.model.*;
import com.hanghae.Today.sHouse.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HeartService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final CommentRepository commentRepository;
    private final HeartCheckRepository heartCheckRepository;



    @Transactional
    public boolean clickToPostHeart(Long postId, Long userId) {
        Post post = getPost(postId);
        User user = getUser(userId);
        boolean toggleLike;

        int likeCnt = post.getHeartCnt();

        //지금 로그인 되어있는 사용자가 해당 포스트에 좋아요를 누른적이 있냐 없냐.
        //게시물과 유저로 좋아요를 찾는다.
        HeartCheck heartCheck = heartCheckRepository.findByPostAndUser(post, user).orElse(null);

        if(heartCheck == null){
            post.setHeartCnt(likeCnt+1);

            HeartCheck hCheck = new HeartCheck(user, post, true);
            heartCheckRepository.save(hCheck);
            toggleLike = true;
        }
        else{
            post.setHeartCnt(likeCnt-1);

            heartCheckRepository.deleteById(heartCheck.getId());
            heartCheck.setPostLikeStatus(false);
            toggleLike = false;
        }
        return toggleLike;
    }


    private User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다.")
        );
        return user;
    }

    private Post getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()->new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        return post;
    }

}
