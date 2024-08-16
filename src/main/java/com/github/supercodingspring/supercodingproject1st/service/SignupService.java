package com.github.supercodingspring.supercodingproject1st.service;

import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.repository.user.UserJpaRepository;
import com.github.supercodingspring.supercodingproject1st.web.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(transactionManager = "tmJpa")
    public ResponseEntity<Map<String, String>> signUp(SignupRequest signupRequest) {
        String email = signupRequest.getEmail(); // 사용자가 입력한 email
        String password = signupRequest.getPassword(); // 사용자가 입력한 비밀번호

        Integer userSerialNum = userJpaRepository.findAll().size() + 1; //현재 저장된 유저 갯수 + 1

        Map<String,String > responseBody = new HashMap<>(); // 응답 객체 생성

        if(email.isEmpty() || password.isEmpty()){ // 아이디나 비밀번호가 빈칸일때
            responseBody.put("message", "이메일, 비밀번호는 필수입니다.");
            return ResponseEntity.badRequest().body(responseBody); // badRequest 응답으로 보냄
        }

        if(userJpaRepository.findByEmail(email) != null){  //user 테이블에 email이 존재하는지 확인
           responseBody.put("message","이미 존재하는 이메일입니다.");
           return ResponseEntity.badRequest().body(responseBody); // badRequest 응답으로 보냄
        }

        User user = User.builder() // 예외처리 후 User 객체 생성
                .email(email)
                .password(passwordEncoder.encode(password)) // 비밀번호 인코딩
                .createdAt(LocalDateTime.now()) // 가입 시간 현재로 지정
                .userName("user" + userSerialNum++) // 변수 사용하여 이름 자동 지정
                .roles("ROLE_USER") // 권한을 위해 String roles 변수에 지정, 추후 Role 객체 등으로 리팩토링 예정
                .build();
        try {
            userJpaRepository.save(user); // JpaRepository를 통해 user 테이블에 저장
            responseBody.put("message", "회원가입이 완료되었습니다.");
            return ResponseEntity.ok(responseBody); // 성공적으로 회원가입 되면
        } catch (Exception e) {
            responseBody.put("message", "회원가입에 실패하였습니다.");
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

}
