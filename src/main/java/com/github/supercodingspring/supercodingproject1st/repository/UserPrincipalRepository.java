package com.github.supercodingspring.supercodingproject1st.repository;

import com.github.supercodingspring.supercodingproject1st.repository.entity.UserPrincipalRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrincipalRepository extends JpaRepository<UserPrincipalRoles, Integer> {

}
