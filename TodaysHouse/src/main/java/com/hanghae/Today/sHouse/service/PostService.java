package com.hanghae.Today.sHouse.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hanghae.Today.sHouse.dto.MultipartFileDto;
import com.hanghae.Today.sHouse.dto.PostRequestDto;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //게시글 등록
    @Transactional
    public Post createPost(UserDetailsImpl userDetails, MultipartFileDto requestDto) {
        User user = userDetails.getUser();

        int size = requestDto.getSize();
        String type = requestDto.getType();
        String style = requestDto.getStyle();
        String area = requestDto.getArea();
        MultipartFile imageUrl = requestDto.getImageUrl();
        String content = requestDto.getContent();

        //s3 관련
        String imgUrl = getImgUrl(imageUrl);

        PostRequestDto postRequestDto = new PostRequestDto(size, type, style, area, imgUrl, content);
        Post post = new Post(user, postRequestDto);
        postRepository.save(post);

        return post;
    }



    ////////////////////////------------S3관련---------------//////////////////////////////
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
}
