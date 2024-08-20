package com.github.supercodingspring.supercodingproject1st.service;
import com.github.supercodingspring.supercodingproject1st.repository.CommentRepository;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import com.github.supercodingspring.supercodingproject1st.repository.post.PostRepository;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotFoundException;
import com.github.supercodingspring.supercodingproject1st.service.mapper.CommentMapper;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentResponseDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentUpdateRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public ResponseEntity<Map<String, Object>> getAllComments() {
        List<CommentResponseDto> comments =  commentRepository.findAll()
                .stream().map(CommentMapper.INSTANCE::CommentToCommentResponseDto).toList();
        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("comments", comments);
        return ResponseEntity.ok(responseBody);
    }

    // 댓글
    @Transactional
    public ResponseEntity<Map<String, String>> createComment(CommentRequestDto dto, HttpServletRequest request) {
        String author = dto.getAuthor();
        String content = dto.getContent();
        Post post = postRepository.findById(dto.getPost_id()).orElse(null);

        Comment comment = Comment.builder()
                .content(content)
                .author(author)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
        return ResponseEntity.ok(Map.of("message", "댓글이 성공적으로 등록되었습니다."));
    }


    public Comment findByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment not found.")
        );
    }


    @Transactional
    public Comment updateCommentById(Long commentId, CommentUpdateRequestDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        comment.setContent(dto.getContent());
        return commentRepository.save(comment);
    }



//
//    @Transactional
//    public void deleteById(Long commentId, String userId) {
//        User user = userRepository.findByUserId(Integer.valueOf(userId));
//        Comment comment = this.findByCommentId(commentId);
//
//        if(!user.equals(comment.getUser())) {
//            throw new IllegalArgumentException("User not found for given userId");
//        }
//        commentRepository.delete(comment);
//    }

}
