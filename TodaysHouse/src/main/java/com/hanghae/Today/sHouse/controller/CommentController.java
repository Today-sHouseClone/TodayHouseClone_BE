package com.hanghae.Today.sHouse.controller;

import com.hanghae.Today.sHouse.dto.CommentRequestDto;
import com.hanghae.Today.sHouse.dto.CommentResponseDto;
import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.repository.CommentRepository;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import com.hanghae.Today.sHouse.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    private final PostRepository postRepository;
    //댓글 등록
    @PostMapping("/api/comment/{postId}")
    public ResponseEntity<String>addComment(@PathVariable Long postId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody CommentRequestDto requestDto){
        try{
            commentService.addComment(postId, userDetails, requestDto);
            return new ResponseEntity<>("댓글 작성 완료하였습니다.", HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //댓글 조회
    @GetMapping("/api/comment/{postId}")
    public ResponseEntity<List<CommentResponseDto>> findComment(@PathVariable Long postId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        return new ResponseEntity<>(commentService.findComment(postId, username), HttpStatus.OK);
    }

    //댓글 수정
    @PutMapping("/api/comment/{commentId}")
    public ResponseEntity<String>updateComment(@PathVariable Long commentId,
                                               @RequestBody CommentRequestDto requestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            commentService.updateComment(commentId, requestDto, userDetails);
            return new ResponseEntity<>("댓글 수정에 성공하셨습니다.", HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //댓글 삭제
    @DeleteMapping("/api/comment/{postId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {

        Optional<Post> fByPostId = postRepository.findById(postId);
        if (fByPostId.isEmpty()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }

        Optional<Comment> fByCommentId = commentRepository.findById(commentId);
        if (fByCommentId.isEmpty()) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }
        commentRepository.deleteById(commentId);
        return new ResponseEntity<>("댓글 삭제에 성공하셨습니다.", HttpStatus.OK);
    }
}
