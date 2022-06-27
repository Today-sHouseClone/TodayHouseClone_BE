package com.hanghae.Today.sHouse.service;

import com.hanghae.Today.sHouse.dto.CommentRequestDto;
import com.hanghae.Today.sHouse.dto.CommentResponseDto;
import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.CommentCheck;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import com.hanghae.Today.sHouse.repository.CommentCheckRepository;
import com.hanghae.Today.sHouse.repository.CommentRepository;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.repository.UserRepository;
import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final CommentCheckRepository commentCheckRepository;
    //댓글 등록
    @Transactional
    public CommentResponseDto addComment(Long postId, UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        User user = userDetails.getUser();
        String userNickname = user.getUserNickname();
        String getComment = requestDto.getComment();
        Post post = findPost(postId);

        if(getComment.equals(""))
            throw new IllegalArgumentException("댓글을 입력해주세요!");

        int commentCnt = post.getCommentCnt();
        post.setCommentCnt(commentCnt+1);
        Comment comment = new Comment(user, post, getComment);
        commentRepository.save(comment);

        Long commentId = comment.getId();
        Boolean commentHeartStatus = comment.getCommentHeartStatus();
        CommentResponseDto commentResponseDto = new CommentResponseDto(postId, commentId, getComment,
                userNickname, commentHeartStatus, comment.getCreatedAt(), comment.getModifiedAt());
        return commentResponseDto;
    }

    //댓글 조회
    public List<CommentResponseDto> findComment(Long postId, UserDetailsImpl userDetails) {
        //Post findPost = findPost(postId);

        List<CommentResponseDto>commentResponseDtoList = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);

        Long userId = userDetails.getUser().getId();

        commentRequestList(postId, commentResponseDtoList, comments, userId);
        return commentResponseDtoList;
    }

    //댓글 페이징
//    public Page<Comment> findComment(Long postId, Pageable pageable) {
//        return commentRepository.findAllByPostId(postId, pageable);
//    }

    //댓글 불러와서 리스트에 저장
    private void commentRequestList(Long postId, List<CommentResponseDto> commentResponseDtoList, List<Comment> comments, Long userId) {
        for(Comment comment : comments){
            Long id = comment.getId();
            String getComment = comment.getComment();
            String userNickname = comment.getUser().getUserNickname();
            Boolean commentHeartStatus = comment.getCommentCheck().stream().filter(c->c.getUser().getId().equals(userId)).findFirst().isPresent();
            LocalDateTime createdAt = comment.getCreatedAt();
            LocalDateTime modifiedAt = comment.getModifiedAt();

            CommentResponseDto commentResponseDto = new CommentResponseDto(postId, id, getComment,  userNickname, commentHeartStatus, createdAt, modifiedAt);
            commentResponseDtoList.add(commentResponseDto);
        }
    }

    //댓글 수정
    @Transactional
    public void updateComment(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comment = findToComment(commentId);

        Long userId = comment.getUser().getId();
        Long currentId = userDetails.getUser().getId();
        String getComment = requestDto.getComment();

        idSameCheck(userId, currentId);

        comment.setComment(getComment);
        commentRepository.save(comment);    //  -> 삭제해도 될 듯?
    }


    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {
        Comment comment = findToComment(commentId);

        int currentCnt = comment.getPost().getCommentCnt();
        comment.getPost().setCommentCnt(currentCnt-1);

        Long userId = comment.getUser().getId();
        Long currentId = userDetails.getUser().getId();

        idSameCheck(userId, currentId);

        commentRepository.deleteById(commentId);
    }

    //댓글 좋아요 구분     @@@@@@@@@@@@@@@@@@@@@@@@@@true -> false -> 삭제가된다 이게문제
    //해결했으나 사용자별로 뜨는게 똑같다. 생각 더 해야한다.   다른 아이디로 누르면 1번 값이 고정된다.
    @Transactional
    public boolean clickToCommentHeart(Long commentId, Long userId) {
        Comment comment = getComment(commentId);
        User user = getUser(userId);

        boolean toggleLike;

        //지금 로그인 되어있는 사용자가 해당 댓글 좋아요를 누른적이 있냐 없냐.
        CommentCheck commentCheck = commentCheckRepository.findByCommentAndUser(comment, user).orElse(null);

        if(commentCheck == null || !commentCheck.getCommentHeartStatus()){
            CommentCheck commentCheck1 = new CommentCheck(user, comment, true);
            commentCheckRepository.save(commentCheck1);
            toggleLike = true;
        }
        else{
            commentCheck.setCommentHeartStatus(false);
            //commentCheckRepository.deleteById(commentId);
            toggleLike = false;
        }
        return toggleLike;
    }

    //댓글찾기
    private Comment getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
        return comment;
    }
    //유저 찾기
    private User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다.")
        );
        return user;
    }

    //아이디 동일 체크
    private void idSameCheck(Long userId, Long currentId) {
        if (!userId.equals(currentId)) {
            throw new IllegalArgumentException("본인이 작성한 글만 수정/삭제 할 수 있습니다.");
        }
    }


    //게시글 찾기
    private Post findPost(Long postId) {
        Post returnPost = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 없습니다.")
        );
        return returnPost;
    }

    //댓글 찾기
    private Comment findToComment(Long commentId) {
        Comment returnComment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("댓글이 없습니다.")
        );
        return returnComment;
    }
}
