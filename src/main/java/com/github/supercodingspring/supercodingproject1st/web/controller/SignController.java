package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.service.LoginService;
import com.github.supercodingspring.supercodingproject1st.service.LogoutService;
import com.github.supercodingspring.supercodingproject1st.service.SignupService;
import com.github.supercodingspring.supercodingproject1st.web.dto.LoginRequest;
import com.github.supercodingspring.supercodingproject1st.web.dto.LogoutRequest;
import com.github.supercodingspring.supercodingproject1st.web.dto.SignupRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
public class SignController {
    private final SignupService signupService;
    private final LoginService loginService;
    private final LogoutService logoutService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody SignupRequest signupRequest) {
        return signupService.signUp(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return loginService.login(loginRequest, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody LogoutRequest logoutRequest, HttpServletResponse response) {
        return logoutService.logout(logoutRequest, response);
    }

}
