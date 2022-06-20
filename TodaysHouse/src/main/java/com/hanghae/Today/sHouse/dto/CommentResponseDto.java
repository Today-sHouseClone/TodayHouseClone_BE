package com.hanghae.Today.sHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponseDto {
    private Long postId;
    private Long id;
    private String comment;
    private String userNickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
