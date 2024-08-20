package com.github.supercodingspring.supercodingproject1st.service.security;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Role;
import com.github.supercodingspring.supercodingproject1st.repository.entity.UserPrincipalRoles;
import com.github.supercodingspring.supercodingproject1st.repository.user.CustomUserDetails;
import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.repository.user.UserRepository;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailFetchJoin(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return CustomUserDetails.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getUserPrincipalRoles().stream().map(UserPrincipalRoles::getRole).map(Role::getName).collect(Collectors.toList()))
                .build();
    }
}
