package com.hanghae.Today.sHouse.service;

import com.hanghae.Today.sHouse.dto.CommentRequestDto;
import com.hanghae.Today.sHouse.dto.CommentResponseDto;
import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import com.hanghae.Today.sHouse.repository.CommentRepository;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        Boolean commentHeartCheck = comment.getCommentHeartCheck();
        CommentResponseDto commentResponseDto = new CommentResponseDto(postId, commentId, getComment, commentHeartCheck,
                userNickname, comment.getCreatedAt(), comment.getModifiedAt());
        return commentResponseDto;
    }

    //댓글 조회
    public List<CommentResponseDto> findComment(Long postId) {
        //Post findPost = findPost(postId);

        List<CommentResponseDto>commentResponseDtoList = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);

        commentRequestList(postId, commentResponseDtoList, comments);
        return commentResponseDtoList;
    }

    //댓글 페이징
//    public Page<Comment> findComment(Long postId, Pageable pageable) {
//        return commentRepository.findAllByPostId(postId, pageable);
//    }

    //댓글 불러와서 리스트에 저장
    private void commentRequestList(Long postId, List<CommentResponseDto> commentResponseDtoList, List<Comment> comments) {
        for(Comment comment : comments){
            Long id = comment.getId();
            String getComment = comment.getComment();
            String userNickname = comment.getUser().getUserNickname();
            Boolean commentHeartCheck = comment.getCommentHeartCheck();
            LocalDateTime createdAt = comment.getCreatedAt();
            LocalDateTime modifiedAt = comment.getModifiedAt();

            CommentResponseDto commentResponseDto = new CommentResponseDto(postId, id, getComment, commentHeartCheck, userNickname, createdAt, modifiedAt);
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
