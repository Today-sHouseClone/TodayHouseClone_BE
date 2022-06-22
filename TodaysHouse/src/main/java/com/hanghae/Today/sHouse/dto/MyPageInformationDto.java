package com.hanghae.Today.sHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageInformationDto {
    private String userNickname;
    private int heartCnt;
    private int bookmarkCnt;
}
