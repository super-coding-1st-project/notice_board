package com.github.supercodingspring.supercodingproject1st.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "posts")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "created_at")
    private String createdAt;
<<<<<<< HEAD
=======
    @Column(name = "like_count")
    private Integer likeCount;
>>>>>>> develop_great

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

<<<<<<< HEAD
=======


>>>>>>> develop_great
    @OneToMany
    @Column(name = "comments_id")
    private List<Comment> comments = new ArrayList<>();

<<<<<<< HEAD
=======


>>>>>>> develop_great
}
