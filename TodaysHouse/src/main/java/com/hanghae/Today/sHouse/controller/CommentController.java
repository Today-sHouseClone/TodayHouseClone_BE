package com.hanghae.Today.sHouse.controller;

import com.hanghae.Today.sHouse.dto.CommentRequestDto;
import com.hanghae.Today.sHouse.dto.CommentResponseDto;
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


@RequiredArgsConstructor
@RestController

public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //댓글 등록
    @PostMapping("/api/comment/{postId}")
    public ResponseEntity<CommentResponseDto>addComment(@PathVariable Long postId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody CommentRequestDto requestDto){
        try{
            return new ResponseEntity<>(commentService.addComment(postId, userDetails, requestDto), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //댓글 조회
    @GetMapping("/api/comment/{postId}")
    public ResponseEntity<List<CommentResponseDto>> findComment(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            return new ResponseEntity<>(commentService.findComment(postId, userDetails), HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //페이징 댓글 조회
//    @GetMapping("/api/comment/{postId}")
//    public Page<CommentResponseDto> getAllComment(@PathVariable Long postId,
//            @PageableDefault (size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<Comment> commentPage = commentService.findComment(postId, pageable);
//        return commentPage.map(CommentResponseDto::from);
//    }

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
    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try{
            commentService.deleteComment(commentId, userDetails);
            return new ResponseEntity<>("댓글 삭제에 성공하셨습니다.", HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
