package com.github.supercodingspring.supercodingproject1st.service.mapper;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Comment;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import com.github.supercodingspring.supercodingproject1st.web.dto.CommentResponseDto;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-22T13:33:44+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 22.0.1 (Oracle Corporation)"
)
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponseDto CommentToCommentResponseDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponseDto.CommentResponseDtoBuilder commentResponseDto = CommentResponseDto.builder();

        commentResponseDto.post_id( commentPostId( comment ) );
        commentResponseDto.created_at( comment.getCreatedAt() );
        commentResponseDto.id( comment.getId() );
        commentResponseDto.content( comment.getContent() );
        commentResponseDto.author( comment.getAuthor() );

        return commentResponseDto.build();
    }

    private Long commentPostId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Post post = comment.getPost();
        if ( post == null ) {
            return null;
        }
        Long id = post.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
