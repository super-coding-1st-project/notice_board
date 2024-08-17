package com.github.supercodingspring.supercodingproject1st.repository.user;

import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u JOIN FETCH u.userPrincipalRoles upr JOIN FETCH upr.role WHERE u.email=:email")
    Optional<User> findByEmailFetchJoin(String email);
    User findByUserId(Integer userId);

    @Query("SELECT u FROM User u JOIN FETCH u.userPrincipalRoles upr JOIN FETCH upr.role WHERE u.userName=:username")
    Optional<User> findUserByUserNameFetchJoin(String username);

}
