package com.github.supercodingspring.supercodingproject1st.config.security;

import com.github.supercodingspring.supercodingproject1st.repository.token.Token;
import com.github.supercodingspring.supercodingproject1st.repository.token.TokenJpaRepository;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final TokenJpaRepository tokenJpaRepository;

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Value("${jwt.secret-key-source}")
    private String secretKeySource;
    private String secretKey;

    @PostConstruct
    public void setUp(){ //
        secretKey = Base64.getEncoder()
                .encodeToString(secretKeySource.getBytes());
    }

    @Value("${TOKEN_VALID_TIME}")
    private long tokenValidTime; //토큰 유효시간 설정, 1000ms * 60 * 60 = 1시간

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

            if(isValidToken(jwtToken) == false) { //DB에 저장된 토큰의 유효성 검사
                throw new JwtException("유효하지 않은 토큰입니다.");
            }

            Date now = new Date(); // 현재 시간을 기준으로 토큰의 만료시간을 확인하기 위함.
            return claims.getExpiration() // 토큰의 만료시간을 Date 객체로 반환 받음
                    .after(now); // 토큰의 만료시간이 현재시간 이후인지 확인, 만료시간이 현재보다 이후라면 true를 반환
        }catch (ExpiredJwtException e){ //토큰이 만료되었을 때
            log.error("만료된 토큰입니다.");
            return false;
        }catch (JwtException | IllegalArgumentException e){
            log.error(e.getMessage());
            return false;
        }
        catch (Exception e){
            return false;
        }
    }

    public void saveTokenStatus(String jwtToken) {
        Token token = Token.builder()
                .token(jwtToken)
                .isValid(true)
                .build();

        tokenJpaRepository.save(token);
    }

    public boolean isValidToken(String jwtToken) { //정상적으로 로그아웃 되어서 DB에 valid가 false인 경우
        List<Token> tokenList = tokenJpaRepository.findAllByToken(jwtToken);
        for (Token token : tokenList) {
            if(Objects.equals(token.getToken(), jwtToken))
                if(token.getIsValid() == false)
                    return false;
        }
        return true;
    }

    public void invalidateToken(String jwtToken) {
        List<Token> tokenList = tokenJpaRepository.findAllByToken(jwtToken);
        for (Token token : tokenList) {
            if(Objects.equals(token.getToken(), jwtToken)) {
                token.setIsValid(false);
                tokenJpaRepository.save(token);
            }
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

    public Authentication createAuthentication(String jwtToken) {
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
