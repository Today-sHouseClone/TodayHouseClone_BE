package com.hanghae.Today.sHouse.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.Today.sHouse.dto.CommentResponseDto;
import com.hanghae.Today.sHouse.dto.MultipartFileDto;
import com.hanghae.Today.sHouse.dto.PostRequestDto;
import com.hanghae.Today.sHouse.dto.PostResponseDto;
import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import com.hanghae.Today.sHouse.repository.CommentRepository;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //전체 게시글 조회
    @Transactional
    public ResponseEntity<PostResponseDto> getAllPost() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto.MainResponse> postResponse = new ArrayList<>();
        for (Post post : posts) {
            Comment viewComment = commentRepository.findTopByPostIdOrderByCreatedAtDesc(post.getId());
            PostResponseDto.MainResponse mainDto = PostResponseDto.MainResponse.builder()
                    .id(post.getId())
                    .userNickname(post.getUser().getUserNickname())
                    .imageUrl(post.getImageUrl())
                    .viewCnt(post.getViewCnt())
                    .content(post.getContent())
                    .heartCnt(post.getHeartCnt())
                    .bookmarkCnt(post.getBookmarkCnt())
                    .commentCnt(post.getCommentCnt())
                    .heartCheck(post.getHeartCheck())
                    .bookmarkCheck(post.getBookmarkCheck())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .commentOne(viewComment)
                    .build();
            postResponse.add(mainDto);
        }
        return new ResponseEntity(postResponse, HttpStatus.OK);
    }

//    public Page<Post> getPosts(Pageable pageable) {
//        return postRepository.findAll(pageable);
//    }

    //게시글 등록
    @Transactional
    public void createPost(UserDetailsImpl userDetails, MultipartFileDto requestDto) {
        User user = userDetails.getUser();

        PostRequestDto postRequestDto = getPostRequestDto(requestDto);

        Post post = new Post(user, postRequestDto);
        postRepository.save(post);
    }

    //게시글 수정
    @Transactional
    public void updatePost(Long postId, MultipartFileDto requestDto, UserDetailsImpl userDetails) {
        Post post = checkPost(postId);

        User user = post.getUser();
        Long userId = user.getId();
        Long currentId = userDetails.getUser().getId();

        idSameCheck(userId, currentId);

        //Url로 변환
        PostRequestDto postRequestDto = getPostRequestDto(requestDto);
        post.update(user, postRequestDto);  //변경감지로 쓰기지연 저장소에 있던 친구들이 DB로 들어간다.(commit 시점에서)
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, UserDetailsImpl userDetails) {
        Post post = checkPost(postId);

        User user = post.getUser();
        Long userId = user.getId();
        Long currentId = userDetails.getUser().getId();

        idSameCheck(userId, currentId);

        String imageUrl = post.getImageUrl();

        deleteImage(imageUrl);
        postRepository.deleteById(postId);
    }

    //게시글 상세조회
    @Transactional
    public PostResponseDto.DetailResponse getDetailsPost(Long postId) {
        Post post = checkPost(postId);

        PostResponseDto.DetailResponse detailResponseDto = PostResponseDto.DetailResponse.builder()
                .id(post.getId())
                .size(post.getSize())
                .type(post.getType())
                .style(post.getStyle())
                .area(post.getArea())
                .heartCnt(post.getHeartCnt())
                .bookmarkCnt(post.getBookmarkCnt())
                .commentCnt(post.getCommentCnt())
                .viewCnt(post.getViewCnt())
                .imageUrl(post.getImageUrl())
                .content(post.getContent())
                .userNickname(post.getUser().getUserNickname())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();

        post.setViewCnt(post.getViewCnt() + 1);
        return detailResponseDto;
    }


    //게시글 유무
    private Post checkPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );
        return post;
    }

    //아이디 동일 체크
    private void idSameCheck(Long userId, Long currentId) {
        if (!userId.equals(currentId)) {
            throw new IllegalArgumentException("본인이 작성한 글만 수정할 수 있습니다.");
        }
    }

    //MultipartFileDto에서 PostRequestDto로 변환해서 전달, s3 접근 후 Multipart -> url+string
    private PostRequestDto getPostRequestDto(MultipartFileDto requestDto) {
        String size = requestDto.getSize();
        String type = requestDto.getType();
        String style = requestDto.getStyle();
        String area = requestDto.getArea();
        MultipartFile imageUrl = requestDto.getImageUrl();
        String content = requestDto.getContent();

        String str_ImgUrl;

        if(imageUrl == null){
            str_ImgUrl=null;
        }else{
            str_ImgUrl = getImgUrl(imageUrl);
        }
        //s3 관련
        return new PostRequestDto(size, type, style, area, str_ImgUrl, content);
    }
    ////////////////////////////////////////////------------S3관련---------------//////////////////////////////////////////////////////
    private String getImgUrl(MultipartFile imageUrl) {
        String fileName = createFileName(imageUrl.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imageUrl.getSize());
        objectMetadata.setContentType(imageUrl.getContentType());

        System.out.println(bucket);

        try(InputStream inputStream = imageUrl.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
