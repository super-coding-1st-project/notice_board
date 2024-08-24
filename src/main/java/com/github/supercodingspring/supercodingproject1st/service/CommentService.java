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
import com.github.supercodingspring.supercodingproject1st.web.dto.DeleteCommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    // 모든 댓글 가져오기
    public List<CommentResponseDto> findAll() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(CommentMapper.INSTANCE::CommentToCommentResponseDto)
                .collect(Collectors.toList());
    }


    // 댓글 id 로 가져오기
    public Comment findByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment not found.")
        );
    }


    // 댓글 생성
    @Transactional
    public Comment createComment(CommentRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        Post post = postRepository.findById(dto.getPostId()).orElse(null);

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .user(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }


    // 댓글 수정
    @Transactional
    public Comment updateCommentById(Long commentId, CommentUpdateRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        Comment comment = commentRepository.findById(commentId).orElse(null);

        assert comment != null;
        if (!user.equals(comment.getUser())) {
            throw new IllegalArgumentException("User not found for given userId");
        }

        comment.setContent(dto.getContent());
        return commentRepository.save(comment);
    }


    // 댓글 삭제
    @Transactional
    public void deleteById(Long commentId, DeleteCommentRequest deleteCommentRequest) {
        User user = userRepository.findByEmail(deleteCommentRequest.getEmail());
        Comment comment = commentRepository.findById(commentId).orElse(null);;

        assert comment != null;
        if(!user.equals(comment.getUser())) {
            throw new IllegalArgumentException("User not found for given userId");
        }
        commentRepository.delete(comment);
    }

}
