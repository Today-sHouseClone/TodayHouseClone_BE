package com.hanghae.Today.sHouse.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentResponseDto {
    private Long postId;
    private Long id;
    private String comment;
    private String userNickname;
    private Boolean commentHeartStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

//    public static CommentResponseDto from(Comment comment) {
//        return CommentResponseDto.builder()
//                .postId(comment.getPost().getId())
//                .id(comment.getId())
//                .comment(comment.getComment())
//                .commentHeartCheck(comment.getCommentHeartCheck())
//                .userNickname(comment.getUser().getUserNickname())
//                .createdAt(comment.getCreatedAt())
//                .modifiedAt(comment.getModifiedAt())
//                .build();
//    }

    @Data
    @AllArgsConstructor
    public static class CommentIdAndTimeDto{
        private Long commentId;
        private LocalDateTime createdAt;
    }
}
