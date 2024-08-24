package com.github.supercodingspring.supercodingproject1st.service;

import com.github.supercodingspring.supercodingproject1st.config.security.JwtTokenProvider;
import com.github.supercodingspring.supercodingproject1st.web.dto.LogoutRequest;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final JwtTokenProvider jwtTokenProvider;

    //프론트엔드에서 클라이언트의 로컬스토리지에 저장된 토큰을 삭제함으로써 로그아웃 기능 구현.
    public void logout(LogoutRequest logoutRequest, HttpServletRequest request, HttpServletResponse response) {

        String jwtToken = request.getHeader("Authorization"); //클라이언트가 요청한 헤더에서 X-AUTH-TOKEN을 가져와 변수에 저장

        if(jwtToken == null || !jwtTokenProvider.validateToken(jwtToken)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        jwtTokenProvider.invalidateToken(jwtToken); //토큰 무효 처리

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //현재 저장된 인증 정보를 로그아웃 처리
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }
}
