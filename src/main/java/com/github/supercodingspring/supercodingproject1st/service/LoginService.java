package com.github.supercodingspring.supercodingproject1st.service;

import com.github.supercodingspring.supercodingproject1st.config.security.JwtTokenProvider;
import com.github.supercodingspring.supercodingproject1st.repository.token.TokenRepository;
import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.repository.user.UserRepository;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotAcceptException;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotFoundException;
import com.github.supercodingspring.supercodingproject1st.web.dto.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public ResponseEntity<Map<String, String>> login(LoginRequest loginRequest, HttpServletResponse response) {
        String email = loginRequest.getEmail(); // 사용자가 입력한 email
        String password = loginRequest.getPassword(); // 사용자가 입력한 password

        Map<String, String> responseBody = new HashMap<>();

        try {
            User user = userRepository.findByEmailFetchJoin(email)
                    .orElseThrow(()->new NotFoundException("User Not Found")); //등록된 사용자인지 검증
            if(user == null) {
                responseBody.put("message", "User를 찾을 수 없습니다");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password) //객체를 생성하여 사용자가 입력한 이메일과 비밀번호를 전달
            );
            SecurityContextHolder.getContext().setAuthentication(authentication); //현재 인증된 사용자의 정보를 저장

            String token = jwtTokenProvider.createToken(email,user.getUserName()); // email과 사용자 이름을 넣은 토큰 생성
            response.setHeader("Authorization", token); //X-AUTH-TOKEN 이라는 헤더 이름으로 토큰을 넣어 설정

            jwtTokenProvider.saveTokenStatus(token);

            responseBody.put("message","성공적으로 로그인하였습니다.");
            return ResponseEntity.ok(responseBody);
        }catch (Exception e){
            throw new NotAcceptException("로그인 할 수 없습니다.");
        }
    }
}
