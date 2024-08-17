package com.github.supercodingspring.supercodingproject1st.repository.token;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findAllByToken(String token);
}
