package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import com.github.supercodingspring.supercodingproject1st.service.CommentService;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentResponseDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentUpdateRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.DeleteCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "CommentController", description = "댓글 관련 controller")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;


    // 모든 댓글 가져오기
    @Operation(summary = "모든 댓글 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 불러왔습니다."),
    })
    @GetMapping("/comments")
    public ResponseEntity<?> getAllComments() {
        log.info("findAllComments");
        List<CommentResponseDto> comments = commentService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("comments", comments);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "댓글 아이디로 댓글 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 불러왔습니다."),
    })
    // 댓글 id로 가져오기
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<?> findByCommentId(@PathVariable Long commentId) {
        Comment comment = commentService.findByCommentId(commentId);
        CommentResponseDto responseDto = CommentResponseDto.toDto(comment);
        return ResponseEntity.ok().body(responseDto);
    }


    // 댓글 작성
    @Operation(summary = "댓글 작성", description = "작성자와 내용을 입력받아 댓글 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 등록했습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/comments")
    public ResponseEntity<?> createComment(
            @RequestBody CommentRequestDto dto, HttpServletRequest request) {

        log.info("author : "+dto.getAuthor()+", content : "+dto.getContent()+", post_id : "+dto.getPostId());

        try {
            Comment comment = commentService.createComment(dto);
            CommentResponseDto newComment = CommentResponseDto.toDto(comment);
            Map<String, Object> response = new HashMap<>();
            response.put("message","댓글이 성공적으로 등록되었습니다.");
            response.put("newComment", newComment);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch(Exception e) {
            // 로깅 등의 추가적인 오류 처리를 고려
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Internal server error"));
        }
    }


    // 댓글 수정
    @Operation(summary = "댓글 수정", description = "수정할 내용을 입력받아 댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 수정했습니다."),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
    @Operation(summary = "댓글 삭제", description = "삭제요청을 받아 댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 삭제했습니다."),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
