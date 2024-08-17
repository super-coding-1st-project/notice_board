package com.github.supercodingspring.supercodingproject1st.web.dto;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentWithChildrenDto {

    private Long commentId;
    private Long postId;
    private String content;
    private String nickname;  // user
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;
    private List<CommentResponseDto> children; // 자식 댓글 리스트 추가

    public static CommentWithChildrenDto toDto(Comment comment) {
        String userName = comment.getUser().getUserName();
        if (userName == null || userName.isBlank()) {
            userName = String.valueOf(comment.getUser().getUserId());
        }

        // 자식 댓글 리스트 변환, null인 경우 빈 리스트 반환
        List<CommentResponseDto> childDtos = comment.getChildren() == null
                ? Collections.emptyList()
                : comment.getChildren().stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());

        return CommentWithChildrenDto.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .userId(String.valueOf(comment.getUser().getUserId()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getModifiedAt())
                .nickname(userName)
                .children(childDtos) // 자식 댓글 리스트 추가
                .build();
    }

}
