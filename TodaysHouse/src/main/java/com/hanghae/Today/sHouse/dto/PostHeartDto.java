package com.hanghae.Today.sHouse.dto;


import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostHeartDto {
    private Post post;
    private User user;
}
