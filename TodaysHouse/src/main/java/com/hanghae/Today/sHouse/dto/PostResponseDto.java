package com.hanghae.Today.sHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

}
