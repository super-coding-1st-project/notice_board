package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.service.GreatService;
import com.github.supercodingspring.supercodingproject1st.web.dto.GreatForPostRequestDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.GreatForPostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GreatController {

    private final GreatService greatService;


    // 좋아요 또는 취소
    // '메시지' & '좋아요' 개수 반환
    @PostMapping("/greats")
    public ResponseEntity<?> createOrDeleteGreat(@RequestBody GreatForPostRequestDto dto, @RequestParam String userId) {
        Boolean isLiked = greatService.likePost(dto.getPostId(), userId);
        Long greatCount = greatService.countGreatByPostId(dto.getPostId());
        System.out.println("##" + greatCount); //
        GreatForPostResponseDto responseDto = GreatForPostResponseDto.toDto(dto.getPostId(), greatCount);
        String message = isLiked ? "좋아요가 완료 되었습니다." : "좋아요가 취소 되었습니다.";

        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("great", responseDto);
        return ResponseEntity.ok().body(response);
    }


    // 게시물 별 좋아요 수 상위 N개 (limit)
//    @GetMapping("/top-greats")
//    public ResponseEntity<?> getTopPosts(@RequestParam(defaultValue = "10") int limit) {
//        try {
//            List<GreatForPostAndUserDto> responseDtos = greatService.getTopPosts(limit);
//            return ResponseEntity.ok(responseDtos);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Internal server error"));
//        }
//    }
//
//    // 유저 별 좋아요 수 상위 N개 (limit)
//    @GetMapping("/top-users")
//    public ResponseEntity<?> getTopUsers(@RequestParam(defaultValue = "10") int limit) {
//        List<User> topUsers = greatService.getTopUsers(limit);
//        return ResponseEntity.ok(topUsers);
//    }




}
