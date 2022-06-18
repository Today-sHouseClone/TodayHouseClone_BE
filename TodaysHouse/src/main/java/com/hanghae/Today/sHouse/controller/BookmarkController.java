package com.hanghae.Today.sHouse.controller;

import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import com.hanghae.Today.sHouse.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookmarkController {
    private final BookmarkService bookmarkService;

    //북마크 클릭
    @PostMapping("/api/bookmark/{postId}")
    public ResponseEntity<Boolean> IsLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUser().getId();
        return new ResponseEntity<>(bookmarkService.clickToBookmark(postId, userId), HttpStatus.OK);
    }
    //지금 로그인한 userId가 Bookmark를 true한것만 가져오기.
}
