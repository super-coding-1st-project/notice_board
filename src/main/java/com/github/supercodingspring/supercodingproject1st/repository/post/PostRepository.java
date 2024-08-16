package com.github.supercodingspring.supercodingproject1st.repository.post;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
