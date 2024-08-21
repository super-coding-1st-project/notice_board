package com.github.supercodingspring.supercodingproject1st.repository.post;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

//   like 데이터를 업데이트하기 위한 jpa function
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    int incrementLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.likeCount = CASE WHEN p.likeCount > 0 THEN p.likeCount - 1 ELSE 0 END WHERE p.id = :postId")
    int decrementLikeCount(@Param("postId") Long postId);



    List<Post> findAllByUser_Email(String email);
    Post findByUser_Email(String email);
}
