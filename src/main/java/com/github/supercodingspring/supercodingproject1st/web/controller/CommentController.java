package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.service.CommentService;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentUpdateRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.DeleteCommentRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    // 모든 댓글 (대댓글 포함) 가져오기
    @GetMapping("/comments")
    public ResponseEntity<Map<String, Object>> getAllComments() {
        log.info("findAllComments");
        return commentService.getAllComments();
    }

    // 댓글 작성
    @PostMapping("/comments")
    public ResponseEntity<Map<String, Object>> createComment(
            @RequestBody CommentRequestDto dto, HttpServletRequest request) {

        log.info("author : "+dto.getAuthor()+", content : "+dto.getContent()+", post_id : "+dto.getPost_id());

        return commentService.createComment(dto, request);
    }


    @PutMapping("/comments/{comment_id}")
    public ResponseEntity<Map<String, Object>> updateCommentById(@PathVariable Long comment_id, @RequestBody CommentUpdateRequestDto dto) {
        return commentService.updateCommentById(comment_id, dto);
    }


    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteByCommentId(@PathVariable Long commentId, @RequestBody DeleteCommentRequest deleteCommentRequest) {
        return commentService.deleteById(commentId, deleteCommentRequest);
    }
}
