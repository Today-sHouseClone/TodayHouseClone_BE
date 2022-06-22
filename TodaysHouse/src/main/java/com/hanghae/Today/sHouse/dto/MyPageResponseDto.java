package com.hanghae.Today.sHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
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
}
