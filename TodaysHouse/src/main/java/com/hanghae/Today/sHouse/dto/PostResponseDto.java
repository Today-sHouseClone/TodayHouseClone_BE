package com.hanghae.Today.sHouse.dto;

import com.hanghae.Today.sHouse.model.Post;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private int size;
    private String type;
    private String style;
    private String area;
    private MultipartFile imageUrl;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    @Data
    public static class MainResponse {
        private String nickName;
        private MultipartFile imageUrl;
        private String content;
        private Long heartCnt;
        private Long bookmarkCnt;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Builder
    @Data
    public static class DetailResponse {
        private int size;
        private String type;
        private String style;
        private String area;
        private Long heartCnt;
        private Long bookmarkCnt;
        private Long commentCnt;
        private Long viewCnt;
        private MultipartFile imageUrl;
        private String content;
        private String nickName;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
