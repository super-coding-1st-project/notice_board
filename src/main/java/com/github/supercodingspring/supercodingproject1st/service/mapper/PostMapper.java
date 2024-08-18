package com.github.supercodingspring.supercodingproject1st.service.mapper;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import com.github.supercodingspring.supercodingproject1st.web.dto.PostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "author", source = "user.userName")
    @Mapping(target = "created_at", source = "createdAt")
    PostDto PosttoPostDto(Post post);
}
