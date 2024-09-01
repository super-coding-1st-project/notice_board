package com.github.supercodingspring.supercodingproject1st.repository;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.repository.entity.UserLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {
    UserLikes findByUserAndPost(User user, Post post);
    Boolean existsByUserAndPost(User user, Post post);
}
