package com.github.supercodingspring.supercodingproject1st.config.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Value("${jwt.secret-key-source}")
    private String secretKeySource;
    private String secretKey;

    @PostConstruct
    public void setUp(){ //
        secretKey = Base64.getEncoder()
                .encodeToString(secretKeySource.getBytes());
    }

    private long tokenValidTime = 1000L * 60 * 60; //토큰 유효시간 설정, 1000ms * 60 * 60 = 1시간

    private final UserDetailsService userDetailsService;

    //클라이언트 요청에서 X-AUTH-TOKEN 헤더에 있는 값 가져오기
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    //토큰 유효성 검증
    public boolean validateToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser() //jwt 파싱하기 위한 빌더 객체 생성.
                    .setSigningKey(this.secretKey) //jwt 검증 시 사용할 비밀키 설정, JWT에 서명된 키와 동일해야함.
                    .parseClaimsJws(jwtToken) //parameter로 받은 jwtToken을 파싱하고 서명을 검증함. 서명이 유효하지 않거나 토큰 형식이 올바르지 않으면 예외 발생.
                    .getBody(); // JWT의 페이로드 부분을 가져옴.
            Date now = new Date(); // 현재 시간을 기준으로 토큰의 만료시간을 확인하기 위함.
            return claims.getExpiration() // 토큰의 만료시간을 Date 객체로 반환 받음
                    .after(now); // 토큰의 만료시간이 현재시간 이후인지 확인, 만료시간이 현재보다 이후라면 true를 반환
        }catch (ExpiredJwtException e){
            log.error("만료된 토큰입니다.");
            return false;
        }catch (JwtException | IllegalArgumentException e){
            log.error("유효하지 않은 토큰이 입력되었습니다.");
            return false;
        }
        catch (Exception e){
            return false;
        }
    }

    public String createToken(String email, String userName){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("user_name", userName);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 일시 : 현재 시간 으로 지정
                .setExpiration(new Date(now.getTime() + this.tokenValidTime)) // 토큰 만료 시간 현재로부터 1시간 지정
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }

    public Authentication getAuthentication(String jwtToken) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserEmail(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private String getUserEmail(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }
}
