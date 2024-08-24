package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import com.github.supercodingspring.supercodingproject1st.service.CommentService;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentResponseDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentUpdateRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.DeleteCommentRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;


    // 모든 댓글 가져오기
    @GetMapping("/comments")
    public ResponseEntity<?> getAllComments() {
        log.info("findAllComments");
        List<CommentResponseDto> comments = commentService.findAll();
        return ResponseEntity.ok().body(comments);
    }

    // 댓글 id로 가져오기
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<?> findByCommentId(@PathVariable Long commentId) {
        Comment comment = commentService.findByCommentId(commentId);
        CommentResponseDto responseDto = CommentResponseDto.toDto(comment);
        return ResponseEntity.ok().body(responseDto);
    }


    // 댓글 작성
    @PostMapping("/comments")
    public ResponseEntity<?> createComment(
            @RequestBody CommentRequestDto dto, HttpServletRequest request) {

        log.info("author : "+dto.getAuthor()+", content : "+dto.getContent()+", post_id : "+dto.getPostId());

        try {
            Comment comment = commentService.createComment(dto);
            CommentResponseDto newComment = CommentResponseDto.toDto(comment);
            Map response = new HashMap();
            response.put("message","댓글이 성공적으로 등록되었습니다.");
            response.put("newComment", newComment);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch(Exception e) {
            // 로깅 등의 추가적인 오류 처리를 고려
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Internal server error"));
        }
    }


    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateCommentById(@PathVariable Long commentId, @RequestBody CommentUpdateRequestDto dto) {
        try {
            Comment comment = commentService.updateCommentById(commentId, dto);
            CommentResponseDto responseDto = CommentResponseDto.toDto(comment);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "댓글이 성공적으로 수정되었습니다.");
            response.put("comment", responseDto);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", e.getMessage()));
        }
    }


    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteByCommentId(@PathVariable Long commentId, @RequestBody DeleteCommentRequest deleteCommentRequest) {
        try {
            commentService.deleteById(commentId, deleteCommentRequest);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "게시글이 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", e.getMessage()));
        }
    }

}
