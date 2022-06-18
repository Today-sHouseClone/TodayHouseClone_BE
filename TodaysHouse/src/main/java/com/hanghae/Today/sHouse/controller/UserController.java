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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


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
        try {
            userService.login(loginRequestDto);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        String token = jwtTokenProvider.createToken(loginRequestDto.getUsername());
        System.out.println(token);
        response.addHeader("Authorization", token);
        return new ResponseEntity<>("로그인에 성공하셨습니다!", HttpStatus.OK);
    }

}
