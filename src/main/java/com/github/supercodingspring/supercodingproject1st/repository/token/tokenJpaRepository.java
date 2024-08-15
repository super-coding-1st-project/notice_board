package com.github.supercodingspring.supercodingproject1st.repository.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenJpaRepository extends JpaRepository<Token, Integer> {
    List<Token> findAllByToken(String token);
}
