package com.hanghae.Today.sHouse.dto;

import com.hanghae.Today.sHouse.model.Comment;
import com.hanghae.Today.sHouse.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentHeartDto {
    private Comment comment;
    private User user;
}