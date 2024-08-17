package com.github.supercodingspring.supercodingproject1st.web.dto;

import com.example.board1.entity.Comment;
import com.example.board1.entity.Post;
import com.example.board1.entity.User;
import com.example.board1.service.PostService;
import com.example.board1.service.UserService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CommentRequestDto {

//    private Long postId;
    private String content;
//    private String userId;







}
