package com.hanghae.Today.sHouse.dto;

import com.hanghae.Today.sHouse.model.Post;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .size(post.getSize())
                .type(post.getType())
                .style(post.getStyle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
