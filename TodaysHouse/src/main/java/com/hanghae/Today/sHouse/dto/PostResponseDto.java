package com.hanghae.Today.sHouse.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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

    private MultipartFile imageUrl;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    @Data
    public static class DetailResponse {
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
        private String nickName;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}
