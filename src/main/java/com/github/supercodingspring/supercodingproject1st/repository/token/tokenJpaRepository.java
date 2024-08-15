package com.github.supercodingspring.supercodingproject1st.repository.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface tokenJpaRepository extends JpaRepository<Token, Integer> {
}
