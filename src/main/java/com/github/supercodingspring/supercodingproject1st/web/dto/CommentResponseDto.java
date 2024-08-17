package com.github.supercodingspring.supercodingproject1st.web.dto;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long commentId;
    private Long postId;
    private String content;
    private String nickname;  // user
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;


    public static CommentResponseDto toDto(Comment comment) {
        String userName = comment.getUser().getUserName();
        if (userName == null || userName.isBlank()) {
            userName = String.valueOf(comment.getUser().getUserId());
        }

        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .userId(String.valueOf(comment.getUser().getUserId()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getModifiedAt())
                .nickname(comment.getUser().getUserName())
                .build();


    }



}
