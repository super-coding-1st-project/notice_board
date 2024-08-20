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

    private Long id;
    private Long post_id;
    private String content;
    private String author;  // user
    private LocalDateTime created_at;
//


}
