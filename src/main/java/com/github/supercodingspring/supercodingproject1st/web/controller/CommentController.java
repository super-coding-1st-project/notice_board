package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import com.github.supercodingspring.supercodingproject1st.service.CommentService;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentUpdateRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
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
    public ResponseEntity<Map<String, String>> createComment(
            @RequestBody CommentRequestDto dto, HttpServletRequest request) {

        log.info("author : "+dto.getAuthor()+", content : "+dto.getContent()+", post_id : "+dto.getPost_id());

        return commentService.createComment(dto, request);
    }


    @PutMapping("/comments/{comment_id}")
    public ResponseEntity<?> updateCommentById(@PathVariable Long comment_id, @RequestBody CommentUpdateRequestDto dto) {
        try {
            Comment comment = commentService.updateCommentById(comment_id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "댓글이 성공적으로 수정되었습니다.");
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", e.getMessage()));
        }
    }

//
//    @DeleteMapping("/comments/{commentId}")
//    public ResponseEntity<?> deleteByCommentId(@PathVariable Long commentId) {
//        try {
//            commentService.deleteById(commentId, userId);
//            return ResponseEntity.ok().body(Collections.singletonMap("message", "게시글이 성공적으로 삭제되었습니다."));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
//        } catch (Exception e) {
//            // 기타 예외 처리
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", e.getMessage()));
//        }
//    }
}
