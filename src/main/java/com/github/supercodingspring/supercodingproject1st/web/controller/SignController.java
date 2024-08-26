package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.service.LoginService;
import com.github.supercodingspring.supercodingproject1st.service.LogoutService;
import com.github.supercodingspring.supercodingproject1st.service.SignupService;
import com.github.supercodingspring.supercodingproject1st.service.exception.InvalidRequestException;
import com.github.supercodingspring.supercodingproject1st.web.dto.LoginRequest;
import com.github.supercodingspring.supercodingproject1st.web.dto.LogoutRequest;
import com.github.supercodingspring.supercodingproject1st.web.dto.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "SignController", description = "회원 관련 Controller")
public class SignController {
    private final SignupService signupService;
    private final LoginService loginService;
    private final LogoutService logoutService;

    @Operation(summary = "회원가입", description = "아이디와 비밀번호, 사용자 이름을 받아 회원가입을 시도합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
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

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 받아 로그인을 시도합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
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

    @Operation(summary = "로그아웃", description = "로그아웃을 시도합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
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
