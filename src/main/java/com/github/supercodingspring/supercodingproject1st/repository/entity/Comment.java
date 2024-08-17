package com.github.supercodingspring.supercodingproject1st.repository.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Table(name = "comment", indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300)
    private String content;

    @ToString.Exclude
    @OneToMany(mappedBy = "comment")
    private List<Great> greats;  // 댓글에 대한 좋아요 목록

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // ------------------------------------------>  대댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;  // 댓글의 부모

    @ToString.Exclude
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();   // 댓글의 자식들 (대댓글)
    // new ArrayList<>()로 초기화하여 NullPointerException 을 방지


    // 자식 댓글을 추가하는 메서드
    public void addChildComment(Comment child) {
        child.setParent(this);
        this.getChildren().add(child);
    }

}
