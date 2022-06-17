package com.hanghae.Today.sHouse.controller;
import com.hanghae.Today.sHouse.dto.SignupRequestDto;
import com.hanghae.Today.sHouse.repository.UserRepository;
import com.hanghae.Today.sHouse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/user/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequestDto requestDto) {
        try{
            userService.registerUser(requestDto);
            return new ResponseEntity<>("로그인에 성공하셨습니다!", HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
