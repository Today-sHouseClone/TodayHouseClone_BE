package com.hanghae.Today.sHouse.service;


import com.hanghae.Today.sHouse.dto.CommentHeartDto;
import com.hanghae.Today.sHouse.dto.PostHeartDto;
import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.Heart;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import com.hanghae.Today.sHouse.repository.CommentRepository;
import com.hanghae.Today.sHouse.repository.HeartRepository;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HeartService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final HeartRepository heartRepository;
    private final CommentRepository commentRepository;

    //Post 좋아요 누르기
    @Transactional
    public boolean clickToPostHeart(Long postId, Long userId) {
        Post post = getPost(postId);
        User user = getUser(userId);

        boolean toggleLike;

        PostHeartDto heartDto = new PostHeartDto(post, user);
        Heart heart = new Heart(heartDto);
        int likeCnt = heart.getPost().getHeartCnt();

        //지금 로그인 되어있는 사용자가 해당 포스트에 좋아요를 누른적이 있냐 없냐.
        Heart findHeart = heartRepository.findByPostAndUser(post, user).orElse(null);

        if(findHeart == null){
            heart.getPost().setHeartCnt(likeCnt+1);

            heartRepository.save(heart);
            toggleLike = true;
        }
        else{
            heart.getPost().setHeartCnt(likeCnt-1);

            heartRepository.deleteById(findHeart.getId());
            toggleLike = false;
        }
        return toggleLike;
    }

    //Comment 좋아요 누르기
    @Transactional
    public boolean clickToCommentHeart(Long commentId, Long userId) {
        Comment comment = getComment(commentId);
        User user = getUser(userId);

        boolean toggleLike;

        CommentHeartDto commentHeartDto = new CommentHeartDto(comment, user);
        Heart heart = new Heart(commentHeartDto);
        int likeCnt = heart.getComment().getCommentHeartCnt();

        //지금 로그인 되어있는 사용자가 해당 포스트에 좋아요를 누른적이 있냐 없냐.
        Heart findHeart = heartRepository.findByCommentAndUser(comment, user).orElse(null);

        if(findHeart == null){
            heart.getComment().setCommentHeartCnt(likeCnt+1);

            heartRepository.save(heart);
            toggleLike = true;
        }
        else{
            heart.getComment().setCommentHeartCnt(likeCnt-1);

            heartRepository.deleteById(findHeart.getId());
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

    private Comment getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
        return comment;
    }
}
