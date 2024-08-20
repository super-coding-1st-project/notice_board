package com.github.supercodingspring.supercodingproject1st.web.dto;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String created_at;
    private String author;
}
