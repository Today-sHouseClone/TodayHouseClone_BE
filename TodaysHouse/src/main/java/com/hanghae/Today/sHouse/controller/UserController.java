package com.hanghae.Today.sHouse.controller;
import com.hanghae.Today.sHouse.dto.LoginRequestDto;
import com.hanghae.Today.sHouse.dto.SignupRequestDto;
import com.hanghae.Today.sHouse.model.User;
import com.hanghae.Today.sHouse.repository.UserRepository;
import com.hanghae.Today.sHouse.security.jwt.JwtTokenProvider;
import com.hanghae.Today.sHouse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

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
        return new ResponseEntity<>(nickName + "로그인에 성공하셨습니다!", HttpStatus.OK);
    }

    //닉네임 중복 체크
    @GetMapping("/api/user/nicknameCheck/{userNickname}")
    public ResponseEntity<String> checkUsername(@PathVariable String userNickname){
        Optional<User> found = userRepository.findByUserNickname(userNickname);
        if(found.isPresent()){
            return new ResponseEntity<>("닉네임이 중복되었습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("축하해요! 닉네임을 사용하실 수 있습니다!.", HttpStatus.OK);
    }
}
