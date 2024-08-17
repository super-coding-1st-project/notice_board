package com.github.supercodingspring.supercodingproject1st.service;
import com.github.supercodingspring.supercodingproject1st.repository.CommentRepository;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.repository.post.PostRepository;
import com.github.supercodingspring.supercodingproject1st.repository.user.UserRepository;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotFoundException;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentWithChildrenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    // 모든 댓글 (대댓글 포함) 가져오기
    public List<CommentWithChildrenDto> findAll() {

        List<Comment> comments = commentRepository.findAll();

        return comments.stream()
                .map(CommentWithChildrenDto::toDto)
                .collect(Collectors.toList());
    }


    // 댓글
    @Transactional
    public Comment createComment(CommentRequestDto dto, Long postId, String userId) {
        User user = userRepository.findByUserId(Integer.valueOf(userId));
        Post post = postRepository.findById(postId).orElse(null);

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .user(user)
                .post(post)
                .build();

        return commentRepository.save(comment);
    }


    // 대댓글 추가
    @Transactional
    public Comment addCommentChild(CommentRequestDto dto, Long parentId, String userId) {
        Comment parent = commentRepository.findById(parentId).orElseThrow(() -> new NotFoundException("Parent comment not found"));
        User user = userRepository.findByUserId(Integer.valueOf(userId));

        Comment child = Comment.builder()
                .content(dto.getContent())
                .user(user)
                .post(parent.getPost()) // 대댓글은 부모 댓글과 같은 게시글
                .build();

        // 자식 댓글을 추가
        parent.addChildComment(child);

        return commentRepository.save(child);
    }

//    public Comment toEntity(CommentRequestDto dto, Long postId, String userId) {
//        Post post = postService.findPostByPostId(postId);
//        User user = userService.findUserByUserId(userId);
//
//        return Comment.builder()
//                .content(dto.getContent())
//                .post(post)
//                .user(user)
//                .build();
//    }


    public Comment findByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment not found.")
        );
    }


    @Transactional
    public Comment updateCommentById(Long commentId, CommentRequestDto dto, String userId) {
        Comment comment = this.findByCommentId(commentId);

        User user = userRepository.findByUserId(Integer.valueOf(userId));
        User commentUser = this.findByCommentId(commentId).getUser();
        if (!user.equals(commentUser)) {
            throw new IllegalArgumentException("User not found for given userId");
        }

        comment.setContent(dto.getContent());
        return commentRepository.save(comment);
    }


    @Transactional
    public void deleteById(Long commentId, String userId) {
        User user = userRepository.findByUserId(Integer.valueOf(userId));
        Comment comment = this.findByCommentId(commentId);

        if(!user.equals(comment.getUser())) {
            throw new IllegalArgumentException("User not found for given userId");
        }
        commentRepository.delete(comment);
    }

}
