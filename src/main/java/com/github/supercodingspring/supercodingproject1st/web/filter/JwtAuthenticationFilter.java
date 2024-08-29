package com.github.supercodingspring.supercodingproject1st.web.filter;

import com.github.supercodingspring.supercodingproject1st.config.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override // 클라이언트 요청이 들어오면 필터를 제일 먼저 거친다.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = jwtTokenProvider.resolveToken(request); //클라이언트 요청에서 JWT토큰을 가져온다.
        String requestURI = request.getRequestURI();              //클라이언트 요청에서 URI를 가져온다.

        // Swagger 관련 경로와 로그인/회원가입 경로는 필터를 건너뛰도록 설정
        if (    requestURI.startsWith("/swagger-ui.html") ||
                requestURI.startsWith("/swagger-ui/") ||
                requestURI.startsWith("/v3/api-docs/") ||
                requestURI.startsWith("/swagger-resources/") ||
                requestURI.startsWith("/webjars/") ||
                "/api/login".equals(requestURI) ||
                "/api/signup".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        if(jwtToken != null && jwtTokenProvider.validateToken(jwtToken)) {
            Authentication auth = jwtTokenProvider.createAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("토큰이 유효하여 인증 절차 진행");
            filterChain.doFilter(request, response);
        }
        else {
            log.error("토큰이 유효하지 않아 인증절차 진행 안함");
            jwtTokenProvider.invalidateToken(jwtToken);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try(PrintWriter out = response.getWriter()) {
                out.print("{\"error\": \"유효하지 않은 토큰\"}");
                out.flush();
            }
        }
    }
}

