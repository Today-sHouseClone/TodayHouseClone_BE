package com.hanghae.Today.sHouse.controller;

import com.hanghae.Today.sHouse.dto.MultipartFileDto;
import com.hanghae.Today.sHouse.dto.PostResponseDto;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import com.hanghae.Today.sHouse.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;
    private final Logger logger = LoggerFactory.getLogger("LoggerController 의 로그");

    //메인페이지 조회
    @GetMapping("/api/posts")
    public ResponseEntity<PostResponseDto> getAllPost() {
        return postService.getAllPost();
    }

    //게시글 등록
    @PostMapping("/api/post")
    public ResponseEntity<String>addPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         MultipartFileDto requestDto){

        try{
            postService.createPost(userDetails, requestDto);
            return new ResponseEntity<>("게시글 등록을 성공하였습니다.", HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //게시글 수정
    @PutMapping("/api/post/{postId}")
    public ResponseEntity<String>updatePost(@PathVariable Long postId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            MultipartFileDto requestDto){
        try{
            postService.updatePost(postId, requestDto, userDetails);
            return new ResponseEntity<>("수정에 성공하셨습니다.", HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //게시글 삭제
    @DeleteMapping("/api/post/{postId}")
    public ResponseEntity<String>deletePost(@PathVariable Long postId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            postService.deletePost(postId, userDetails);
            return new ResponseEntity("삭제에 성공하셨습니다.", HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //게시글 상세조회
    @GetMapping("/api/post/{postId}")
    public ResponseEntity<PostResponseDto> getDetailsPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity(postService.getDetailsPost(postId, userDetails), HttpStatus.OK);
    }

    //조회수 랭킹 Jsonignore 어노테이션을 활용해서 순수 post만 꺼내왔다
    @GetMapping("/api/post/ranking")
    public ResponseEntity<List<Post>> getPostRanking(){
        return new ResponseEntity<>(postRepository.findAllByOrderByViewCntDesc(), HttpStatus.OK);
    }
}
