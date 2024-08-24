package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.service.LoginService;
import com.github.supercodingspring.supercodingproject1st.service.LogoutService;
import com.github.supercodingspring.supercodingproject1st.service.SignupService;
import com.github.supercodingspring.supercodingproject1st.service.exception.InvalidRequestException;
import com.github.supercodingspring.supercodingproject1st.web.dto.LoginRequest;
import com.github.supercodingspring.supercodingproject1st.web.dto.LogoutRequest;
import com.github.supercodingspring.supercodingproject1st.web.dto.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
@Slf4j
public class SignController {
    private final SignupService signupService;
    private final LoginService loginService;
    private final LogoutService logoutService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        Map<String,String > responseBody = new HashMap<>(); // 응답 객체 생성
        try{
            signupService.signUp(signupRequest);
            responseBody.put("message", "회원가입이 완료되었습니다.");
            return ResponseEntity.ok(responseBody);
        }catch (Exception e){
            log.error(e.getMessage());
            responseBody.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Map<String, String> responseBody = new HashMap<>();
        try {
            loginService.login(loginRequest, response);
            responseBody.put("message", "성공적으로 로그인하였습니다.");
            return ResponseEntity.ok(responseBody);
        }catch (Exception e){
            log.error(e.getMessage());
            responseBody.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> responseBody = new HashMap<>();
        try{
            logoutService.logout(logoutRequest, request, response);
            responseBody.put("message", "로그아웃 되었습니다.");
            return ResponseEntity.ok(responseBody);
        }catch (Exception e){
            log.error(e.getMessage());
            responseBody.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(responseBody);
        }
    }

}
