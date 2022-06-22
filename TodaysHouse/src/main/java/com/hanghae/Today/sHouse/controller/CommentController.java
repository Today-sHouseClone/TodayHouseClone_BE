package com.hanghae.Today.sHouse.controller;

import com.hanghae.Today.sHouse.dto.CommentRequestDto;
import com.hanghae.Today.sHouse.dto.CommentResponseDto;
import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.repository.CommentRepository;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import com.hanghae.Today.sHouse.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@RestController

public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    //댓글 등록
    @PostMapping("/api/comment/{postId}")
    public ResponseEntity<LocalDateTime>addComment(@PathVariable Long postId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody CommentRequestDto requestDto){
        try{
            LocalDateTime localDateTime = commentService.addComment(postId, userDetails, requestDto);
            return new ResponseEntity<>(localDateTime , HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //댓글 조회
    @GetMapping("/api/comment/{postId}")
    public ResponseEntity<List<CommentResponseDto>> findComment(
            @PathVariable Long postId/*, @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable*/){
        try{
            return new ResponseEntity<>(commentService.findComment(postId), HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
