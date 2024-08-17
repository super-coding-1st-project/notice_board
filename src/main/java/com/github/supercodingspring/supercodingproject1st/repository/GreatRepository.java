package com.github.supercodingspring.supercodingproject1st.repository;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Great;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GreatRepository extends JpaRepository<Great, Long> {

    @Query("SELECT g FROM Great g WHERE g.post.id = :postId AND g.user.userId = :userId")
    Great findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") String userId);

    @Query("SELECT COUNT(g) FROM Great g WHERE g.post.id = :postId")
    Long countGreatByPostId(Long postId);

    // 게시물 별 좋아요 수 상위 N개
//    @Query("SELECT g.post, COUNT(g) as cnt FROM Great g GROUP BY g.post ORDER BY cnt DESC")
//    List<Object[]> findTopPosts(Pageable pageable);

    @Query("SELECT g.post.id, g.user.userId, COUNT(g) as cnt FROM Great g GROUP BY g.post.id, g.user.userId ORDER BY cnt DESC")
    List<Object[]> findTopPosts(Pageable pageable);

    // 유저 별 좋아요 수 상위 N개
    @Query("SELECT g.user, COUNT(g) as cnt FROM Great g GROUP BY g.user ORDER BY cnt DESC")
    List<Object[]> findTopUsers(Pageable pageable);



}
