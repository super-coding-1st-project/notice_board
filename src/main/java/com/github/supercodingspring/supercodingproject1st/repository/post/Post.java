package com.github.supercodingspring.supercodingproject1st.repository.post;

import com.github.supercodingspring.supercodingproject1st.repository.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "post")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "created_at")
    private String created_at;

    @Column(name = "author")
    private String author;
}
