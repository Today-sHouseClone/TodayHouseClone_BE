package com.hanghae.Today.sHouse.controller;


import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import com.hanghae.Today.sHouse.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HeartController {
    private final HeartService heartService;

    //좋아요 클릭
    @PostMapping("/api/like/{postId}")
    public ResponseEntity<Boolean> IsHeart(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            Long userId = userDetails.getUser().getId();
            return new ResponseEntity<>(heartService.clickToHeart(postId, userId), HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
