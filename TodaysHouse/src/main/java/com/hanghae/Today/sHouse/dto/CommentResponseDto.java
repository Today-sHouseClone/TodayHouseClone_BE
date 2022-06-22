package com.hanghae.Today.sHouse.dto;

import com.hanghae.Today.sHouse.model.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentResponseDto {
    private Long postId;
    private Long id;
    private String comment;
    private Boolean commentHeartCheck;
    private String userNickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .postId(comment.getId())
                .id(comment.getId())
                .comment(comment.getComment())
                .commentHeartCheck(comment.getCommentHeartCheck())
                .userNickname(comment.getUser().getUserNickname())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
