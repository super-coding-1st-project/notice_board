package com.github.supercodingspring.supercodingproject1st.service;
import com.github.supercodingspring.supercodingproject1st.repository.CommentRepository;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.repository.post.PostRepository;
import com.github.supercodingspring.supercodingproject1st.repository.user.UserRepository;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotFoundException;
import com.github.supercodingspring.supercodingproject1st.service.mapper.CommentMapper;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentResponseDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentUpdateRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.DeletePostRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Map<String, Object>> getAllComments() {
        List<CommentResponseDto> comments =  commentRepository.findAll()
                .stream().map(CommentMapper.INSTANCE::CommentToCommentResponseDto).toList();
        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("comments", comments);
        return ResponseEntity.ok(responseBody);
    }

    // 댓글
    @Transactional
    public ResponseEntity<Map<String, Object>> createComment(CommentRequestDto dto, HttpServletRequest request) {
        String author = dto.getAuthor();
        String content = dto.getContent();
        User user = userRepository.findByEmail(dto.getEmail());
        Post post = postRepository.findById(dto.getPost_id()).orElse(null);

        Integer id = user.getUserId();

        Comment comment = Comment.builder()
                .content(content)
                .author(author)
                .user(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "댓글이 성공적으로 등록되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> updateCommentById(Long commentId, CommentUpdateRequestDto dto) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByEmail(dto.getEmail());
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getUser().equals(user)) {
            log.info(String.valueOf(comment.getUser()), user);
            comment.setContent(dto.getContent());
            try {
                commentRepository.save(comment);
                response.put("message", "댓글이 성공적으로 수정되었습니다.");
                return ResponseEntity.ok().body(response);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
            } catch (Exception e) {
                // 기타 예외 처리
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", e.getMessage()));
            }
        } else {
            response.put("message", "권한이 없습니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> deleteById(Long commentId, DeletePostRequest deletePostRequest) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByEmail(deletePostRequest.getEmail());
        Comment comment = commentRepository.findById(commentId).get();
        if(comment.getUser().equals(user)) {
            try {
                commentRepository.deleteById(commentId);
                return ResponseEntity.ok().body(Collections.singletonMap("message", "댓글이 성공적으로 삭제되었습니다."));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
            } catch (Exception e) {
                // 기타 예외 처리
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", e.getMessage()));
            }
        }
        else {
            response.put("message", "권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }
}
