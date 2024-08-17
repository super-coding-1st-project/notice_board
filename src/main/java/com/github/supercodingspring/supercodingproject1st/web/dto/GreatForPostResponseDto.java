package com.github.supercodingspring.supercodingproject1st.web.dto;


import lombok.*;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GreatForPostResponseDto {

    private Long postId;
    private Long greatsCount;

    public static GreatForPostResponseDto toDto(Long postId, Long greatsCount) {

        return GreatForPostResponseDto.builder()
                .postId(postId)
                .greatsCount(greatsCount)
                .build();
    }

}
