package com.github.supercodingspring.supercodingproject1st.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
