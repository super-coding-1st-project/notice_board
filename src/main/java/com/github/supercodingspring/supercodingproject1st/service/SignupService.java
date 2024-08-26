package com.github.supercodingspring.supercodingproject1st.service;

import com.github.supercodingspring.supercodingproject1st.repository.UserPrincipalRepository;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Role;
import com.github.supercodingspring.supercodingproject1st.repository.entity.RolesRepository;
import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.repository.entity.UserPrincipalRoles;
import com.github.supercodingspring.supercodingproject1st.repository.user.UserRepository;
import com.github.supercodingspring.supercodingproject1st.service.exception.InvalidRequestException;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotFoundException;
import com.github.supercodingspring.supercodingproject1st.web.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final UserPrincipalRepository userPrincipalRepository;

    @Transactional(transactionManager = "tmJpa")
    public void signUp(SignupRequest signupRequest) {
        String email = signupRequest.getEmail(); // 사용자가 입력한 email
        String password = signupRequest.getPassword(); // 사용자가 입력한 비밀번호
        String username = signupRequest.getUsername();

        if(email.isEmpty() || password.isEmpty() || username.isEmpty()){ // 아이디나 비밀번호가 빈칸일때
            log.error("email or password or username is empty");
            throw new InvalidRequestException("이메일, 비밀번호, 사용자이름은 필수입니다.");
        }

        if(userRepository.findByEmailFetchJoin(email).isPresent()){  //user 테이블에 email이 존재하는지 확인
            log.error("email is already in use");
            throw new DataIntegrityViolationException("사용중인 이메일입니다.");
        }

        Role role = rolesRepository.findByName("ROLE_USER");

        User user = User.builder() // 예외처리 후 User 객체 생성
                .email(email)
                .password(passwordEncoder.encode(password)) // 비밀번호 인코딩
                .createdAt(LocalDateTime.now()) // 가입 시간 현재로 지정
                .userName(username) // 변수 사용하여 이름 자동 지정
                .build();
        userRepository.save(user);
        userPrincipalRepository.save(UserPrincipalRoles.builder()
                .role(role)
                .user(user)
                .build());

        userRepository.save(user);
    }

}
