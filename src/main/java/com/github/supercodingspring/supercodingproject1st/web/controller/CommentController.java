package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import com.github.supercodingspring.supercodingproject1st.service.CommentService;
import com.github.supercodingspring.supercodingproject1st.service.exception.InvalidRequestException;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentResponseDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentWithChildrenDto;
import lombok.RequiredArgsConstructor;
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

    private final CommentService commentService;


    // 모든 댓글 (대댓글 포함) 가져오기
    @GetMapping("/comments")
    public ResponseEntity<?> findAllComments() {
        List<CommentWithChildrenDto> comments = commentService.findAll();
        return ResponseEntity.ok().body(comments);
    }

    // 댓글 작성
    @PostMapping("/comments")
    public ResponseEntity<?> createComment(
            @RequestBody CommentRequestDto dto, @RequestParam Long postId, @RequestParam String userId) {

        try {
            Comment comment = commentService.createComment(dto, postId, userId);
            CommentResponseDto newComment = CommentResponseDto.toDto(comment);
            Map response = new HashMap();
            response.put("message","댓글이 성공적으로 작성되었습니다.");
            response.put("newComment", newComment);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidRequestException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "유저 또는 입력 데이터가 유효하지 않습니다."));
        } catch (Exception e) {
            // 로깅 등의 추가적인 오류 처리를 고려
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Internal server error"));
        }

    }


    // 대댓글 작성
    @PostMapping("/comments/reply")
    public ResponseEntity<?> createCommentChild(
            @RequestBody CommentRequestDto dto, @RequestParam Long parentId, @RequestParam String userId) {
        try {
            Comment comment = commentService.addCommentChild(dto, parentId, userId);
            CommentWithChildrenDto newComments = CommentWithChildrenDto.toDto(comment);

            Map response = new HashMap();
            response.put("message","댓글이 성공적으로 작성되었습니다.");
            response.put("newComments", newComments);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidRequestException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "유저 또는 입력 데이터가 유효하지 않습니다."));
        } catch (Exception e) {
            // 로깅 등의 추가적인 오류 처리를 고려
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Internal server error"));
        }

    }



    @GetMapping("/comments/{commentId}")
    public ResponseEntity<?> findByCommentId(@PathVariable Long commentId) {
        Comment comment = commentService.findByCommentId(commentId);
        CommentResponseDto responseDto = CommentResponseDto.toDto(comment);
        return ResponseEntity.ok().body(responseDto);
    }


    @PostMapping("/comments/{commentId}/update")
    public ResponseEntity<?> updateCommentById(@PathVariable Long commentId, @RequestBody CommentRequestDto dto, @RequestParam String userId) {
        try {
            Comment comment = commentService.updateCommentById(commentId, dto, userId);
            CommentResponseDto responseDto = CommentResponseDto.toDto(comment);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Comment updated successfully");
            response.put("comment", responseDto);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", e.getMessage()));
        }
    }


    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteByCommentId(@PathVariable Long commentId, @RequestParam String userId) {
        try {
            commentService.deleteById(commentId, userId);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "게시글이 성공적으로 삭제되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", e.getMessage()));
        }
    }




}
