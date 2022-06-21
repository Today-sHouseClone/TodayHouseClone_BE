package com.hanghae.Today.sHouse.dto;

import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.Post;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String size;
    private String type;
    private String style;
    private String area;

    private String imageUrl;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    @Data
    public static class MainResponse {
        private Long id;
        private String userNickname;
        private String imageUrl;
        private String content;

        private int viewCnt;
        private int heartCnt;
        private int bookmarkCnt;
        private int commentCnt;

        private List<Comment> commentOne;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Builder
    @Data
    public static class DetailResponse {
        private Long id;
        private String size;
        private String type;
        private String style;
        private String area;

        private int heartCnt;
        private int bookmarkCnt;
        private int commentCnt;
        private int viewCnt;

        private String imageUrl;
        private String content;
        private String userNickname;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}