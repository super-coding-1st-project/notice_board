package com.github.supercodingspring.supercodingproject1st.repository.entity;

import com.github.supercodingspring.supercodingproject1st.repository.UserLikesRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserLikes> userLikes = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<UserPrincipalRoles> userPrincipalRoles;
}
