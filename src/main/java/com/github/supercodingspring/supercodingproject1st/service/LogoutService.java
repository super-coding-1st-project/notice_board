package com.github.supercodingspring.supercodingproject1st.service;

import com.github.supercodingspring.supercodingproject1st.web.dto.LogoutRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogoutService {

    public ResponseEntity<Map<String, String>> logout(LogoutRequest logoutRequest, HttpServletResponse response) {
        String jwtToken = response.getHeader("X-AUTH-TOKEN"); //클라이언트가 요청한 헤더에서 X-AUTH-TOKEN을 가져와 변수에 저장

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message","로그아웃 되었습니다.");

        return ResponseEntity.ok(responseBody);
        // 추후 jwtToken을 블랙리스트 등록하여 만료처리 예정
    }
}
