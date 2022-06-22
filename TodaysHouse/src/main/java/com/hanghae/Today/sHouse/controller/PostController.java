package com.hanghae.Today.sHouse.controller;

import com.hanghae.Today.sHouse.dto.CommentResponseDto;
import com.hanghae.Today.sHouse.dto.MultipartFileDto;
import com.hanghae.Today.sHouse.dto.PostRankingDto;
import com.hanghae.Today.sHouse.dto.PostResponseDto;
import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import com.hanghae.Today.sHouse.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import java.util.List;
import java.util.stream.Collectors;

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

//    @GetMapping("/api/posts")
//    public Page<PostResponseDto.MainResponse> getAllPost(
//            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<Post> postPage = postService.getPosts(pageable);
//        return postPage.map(PostResponseDto.MainResponse::from);
//    }

    //게시글 등록
    @PostMapping("/api/post")
    public ResponseEntity<String>addPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         MultipartFileDto requestDto){
        try{
            System.out.println(userDetails.getUsername());
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
    public ResponseEntity<PostResponseDto> getDetailsPost(@PathVariable Long postId) {
        try{
            return new ResponseEntity(postService.getDetailsPost(postId), HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    //조회수 랭킹 Jsonignore 어노테이션을 활용해서 순수 post만 꺼내왔다
    //transactional 어노테이션!!!
    @GetMapping("/api/post/ranking")
    public ResponseEntity<List<PostRankingDto>> getPostRanking(){
        Pageable pageable = PageRequest.of(0, 3, Sort.Direction.DESC, "viewCnt");

        List<Post> allByPostRanking = postRepository.findAllByPostRanking(pageable);
        List<PostRankingDto>postRankingDtoList = allByPostRanking.stream()
                .map((p)-> new PostRankingDto(p.getId(), p.getImageUrl(),p.getUser().getUserNickname()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(postRankingDtoList, HttpStatus.OK);
    }
}
