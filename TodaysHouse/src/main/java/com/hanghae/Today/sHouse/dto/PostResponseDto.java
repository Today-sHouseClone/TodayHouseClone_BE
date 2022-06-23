package com.hanghae.Today.sHouse.dto;

import com.hanghae.Today.sHouse.model.Comment;
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
        private Boolean heartCheck;
        private Boolean bookmarkCheck;

        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private Comment commentOne;

//        public static MainResponse from(Post post) {
//            return MainResponse.builder()
//                    .id(post.getId())
//                    .userNickname(post.getUser().getUserNickname())
//                    .imageUrl(post.getImageUrl())
//                    .content(post.getContent())
//                    .viewCnt(post.getViewCnt())
//                    .heartCnt(post.getHeartCnt())
//                    .bookmarkCnt(post.getBookmarkCnt())
//                    .commentCnt(post.getCommentCnt())
//                    .heartCheck(post.getHeartCheck())
//                    .bookmarkCheck(post.getBookmarkCheck())
//                    .createdAt(post.getCreatedAt())
//                    .modifiedAt(post.getModifiedAt())
//                    //.commentOne(viewComment)
//                    .build();
//        }
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