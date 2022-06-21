package com.hanghae.Today.sHouse.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponseDto {
    private Long postId;
    private Long id;
    private String comment;
    private int commentHeartCnt;
    private String userNickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
