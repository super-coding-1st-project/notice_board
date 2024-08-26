package com.github.supercodingspring.supercodingproject1st.web.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CommentRequestDto {
    private String author;
    private String email;
    private String content;
    private Long postId;
}
