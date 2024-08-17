package com.github.supercodingspring.supercodingproject1st.web.dto;


import lombok.*;

import java.util.List;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GreatForPostAndUserDto {

    private Long postId;
    private Long greatsCount;
    private String userId;


    public static GreatForPostAndUserDto toDto(Long postId, String userId, Long greatsCount) {

        return GreatForPostAndUserDto.builder()
                .postId(postId)
                .greatsCount(greatsCount)
                .userId(userId)
                .build();
    }




}
