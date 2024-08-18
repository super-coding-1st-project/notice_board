package com.github.supercodingspring.supercodingproject1st.repository;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findByPostId(Long postId);
}
