package com.hanghae.Today.sHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MultipartFileDto {
    private int size;
    private String type;
    private String style;
    private String area;
    private MultipartFile imageUrl;
    private String content;
}
