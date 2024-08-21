package com.github.supercodingspring.supercodingproject1st.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "userLikes")
public class UserLikes {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public UserLikes(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Override
    public String toString() {
        return "UserLikes{" +
                "id=" + id +
                ", user=" + user.getUserName() +
                ", post=" + post.getId() +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof UserLikes userLikes)) return false;
        return Objects.equals(user, userLikes.user) && Objects.equals(post, userLikes.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, post);
    }
}
