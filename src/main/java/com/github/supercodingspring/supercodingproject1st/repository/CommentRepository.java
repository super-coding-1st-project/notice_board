package com.github.supercodingspring.supercodingproject1st.repository;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
