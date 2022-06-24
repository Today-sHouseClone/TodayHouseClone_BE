package com.hanghae.Today.sHouse.controller;
import com.hanghae.Today.sHouse.dto.*;
import com.hanghae.Today.sHouse.model.Post;
import com.hanghae.Today.sHouse.model.User;
import com.hanghae.Today.sHouse.repository.PostRepository;
import com.hanghae.Today.sHouse.repository.UserRepository;
import com.hanghae.Today.sHouse.security.UserDetailsImpl;
import com.hanghae.Today.sHouse.security.jwt.JwtTokenProvider;
import com.hanghae.Today.sHouse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostRepository postRepository;

    //닉네임 뿌려주기
    @GetMapping("/user/nickname")
    public ResponseEntity<String> getUserNickname(@AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            return new ResponseEntity<>(userDetails.getUser().getUserNickname(), HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    //회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequestDto requestDto) {
        try{
            userService.registerUser(requestDto);
            return new ResponseEntity<>("회원가입에 성공하셨습니다!", HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 회원 로그인
    @PostMapping("/user/login")
    public ResponseEntity<String> login(final HttpServletResponse response, @RequestBody LoginRequestDto loginRequestDto) {
        String nickName;
        try {
            nickName = userService.login(loginRequestDto);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        String token = jwtTokenProvider.createToken(loginRequestDto.getUsername());
        System.out.println(token);
        response.addHeader("Authorization", token);
        return new ResponseEntity<>(nickName + "님! 로그인에 성공하셨습니다.", HttpStatus.OK);
    }


    //닉네임 중복 체크
    @GetMapping("/api/user/nicknameCheck/{userNickname}")
    public ResponseEntity<String> checkUsername(@PathVariable String userNickname){
        Optional<User> found = userRepository.findByUserNickname(userNickname);
        if(found.isPresent()){
            return new ResponseEntity<>("닉네임이 중복되었습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("축하해요! 닉네임을 사용하실 수 있습니다!", HttpStatus.OK);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    //마이페이지 내 사진
    @GetMapping("/api/post/mypage/picture")
    public ResponseEntity<List<MypagePictureDto>> getPostRanking(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUser().getId();

        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.DESC, "createdAt");

        List<Post> byMypagePicture = postRepository.findByMypagePicture(pageable, userId);
        List<MypagePictureDto>mypagePictureDtoList = byMypagePicture.stream()
                .map((p)-> new MypagePictureDto(p.getImageUrl()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(mypagePictureDtoList, HttpStatus.OK);
    }

    //마이페이지 전체보기
    @GetMapping("/api/post/mypage/pictures")
    public ResponseEntity<List<MyPageResponseDto>> getMyPagePosts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUser().getId();
        String userNickname = userDetails.getUser().getUserNickname();
        List<Post> byMypagePosting = postRepository.findByMypagePosting(userId);
        List<MyPageResponseDto>mypagePictureDtoList = byMypagePosting.stream()
                .map((p)-> new MyPageResponseDto(p.getId(), userNickname, p.getImageUrl(), p.getContent(), p.getViewCnt(),
                                p.getHeartCnt(), p.getBookmarkCnt(), p.getCommentCnt(),
                                p.getHeartCheck().stream().filter(hc->hc.getUser().getId().equals(userId)).findFirst().isPresent(),
                                p.getBookmark().stream().filter(hc->hc.getUser().getId().equals(userId)).findFirst().isPresent(),
                        p.getCreatedAt(), p.getModifiedAt()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(mypagePictureDtoList, HttpStatus.OK);
    }

    //마이페이지 미니정보3종세트
    @GetMapping("/api/post/mypage/information")
    public ResponseEntity<MyPageInformationDto>getMyPageInformation(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUser().getId();
        String userNickname = userDetails.getUser().getUserNickname();
        int byUserHeartCnt = postRepository.findByUserHeartCnt(userId);
        int byUserBookmarkCnt = postRepository.findByUserBookmarkCnt(userId);

        MyPageInformationDto myPageInformationDto = new MyPageInformationDto(userNickname, byUserHeartCnt, byUserBookmarkCnt);
        return new ResponseEntity<>(myPageInformationDto, HttpStatus.OK);
    }
}
