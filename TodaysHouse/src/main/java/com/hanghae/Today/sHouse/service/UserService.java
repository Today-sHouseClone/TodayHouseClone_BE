package com.hanghae.Today.sHouse.service;

import com.hanghae.Today.sHouse.dto.SignupRequestDto;
import com.hanghae.Today.sHouse.model.User;
import com.hanghae.Today.sHouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@RestController
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //닉네임 중복으로 바꾸기, 컬럼명 수정, 이메일 중복
    @Transactional
    public void registerUser(SignupRequestDto requestDto){
        // 회원 ID 중복 확인
        String userEmail = requestDto.getUsername();
        String userNickname = requestDto.getUserNickname();
        String userPassword = requestDto.getPassword();

        Optional<User> found = userRepository.findByUsername(userEmail);
        //- 데이터베이스에 존재하는 닉네임을 입력한 채 회원가입 버튼을 누른 경우 "중복된 닉네임입니다." 라는 에러메세지
        if (found.isPresent())
            throw new IllegalArgumentException("중복된 이메일입니다.");

        //- 닉네임은 `최소 3자 이상, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성하기
        String pattern = "^[a-zA-Z0-9가-힣]*$";
        if(userNickname.length() < 2 && Pattern.matches(pattern, userNickname))
            throw new IllegalArgumentException("영문/숫자 포함 닉네임은 2자리 이상 입력해주세요.");

        String password = passwordEncoder.encode(requestDto.getPassword());

        //- 비밀번호는 `최소 4자 이상이며, 닉네임과 같은 값이 포함된 경우 회원가입에 실패`로 만들기
        if(userPassword.length() < 8 || userPassword.contains(userNickname))
            throw new IllegalArgumentException("비밀번호 8자리 이상, 혹은 닉네임과 같은 값을 사용할 수 없습니다.");

        String nickname = requestDto.getUserNickname();

        //데이터 저장
        User user = new User(userEmail, nickname, password);
        userRepository.save(user);
    }
}
