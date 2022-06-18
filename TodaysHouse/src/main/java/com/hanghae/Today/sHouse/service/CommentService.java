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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    //댓글 등록
    @Transactional
    public void addComment(Long postId, UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        User user = userDetails.getUser();
        String getComment = requestDto.getComment();

        if(getComment.equals(""))
            throw new IllegalArgumentException("댓글을 입력해주세요!");

        Post post = findPost(postId);

        int commentCnt = post.getCommentCnt();
        post.setCommentCnt(commentCnt+1);

        Comment comment = new Comment(user, post, getComment);
        commentRepository.save(comment);
    }

    //댓글 조회
    public List<CommentResponseDto> findComment(Long postId, String username) {
        Post findPost = findPost(postId);

        List<CommentResponseDto>commentResponseDtoList = new ArrayList<>();
        List<Comment> comments = findPost.getComments();

        commentRequestList(postId, username, commentResponseDtoList, comments);
        return commentResponseDtoList;
    }

    //댓글 수정
    @Transactional
    public void updateComment(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comment = findComment(commentId);

        String username = comment.getUser().getUsername();
        String currentUsername = userDetails.getUsername();
        String updateComment = requestDto.getComment();


        //방금 저장한 이름이랑 수정을 신청한 이름이랑 같다면
        if(username.equals(currentUsername)){
            comment.setComment(updateComment);
            commentRepository.save(comment);
        }
        else{
            throw new IllegalArgumentException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }
    }



    //댓글 불러와서 리스트에 저장
    private void commentRequestList(Long postId, String username, List<CommentResponseDto> commentResponseDtoList, List<Comment> comments) {
        for(Comment comment : comments){
            Long id = comment.getId();
            String getComment = comment.getComment();
            LocalDateTime createdAt = comment.getCreatedAt();
            LocalDateTime modifiedAt = comment.getModifiedAt();

            CommentResponseDto commentResponseDto = new CommentResponseDto(postId, id, getComment, username, createdAt, modifiedAt);

            commentResponseDtoList.add(commentResponseDto);
        }
    }


    //게시글 찾기
    private Post findPost(Long postId) {
        Post returnPost = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("게시글이 없습니다."));
        return returnPost;
    }

    //댓글 찾기
    private Comment findComment(Long commentId) {
        Comment returnComment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("댓글이 없습니다.")
        );
        return returnComment;
    }




}
