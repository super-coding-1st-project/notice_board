package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.repository.post.Post;
import com.github.supercodingspring.supercodingproject1st.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@Tag(name = "PostController", description = "게시글 관련 Controller")
public class PostController {
    private final PostService postService;

    @Operation(summary = "클라이언트로부터 요청받아 게시글 DB에 저장")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 저장했습니다."
    )
    @PostMapping("/posts")
    public ResponseEntity<Map<String, String>> savePost(@RequestBody Post post, HttpServletRequest request){
        return postService.savePost(post, request);
    }

    @GetMapping("/posts")
    public ResponseEntity<Map<String, Object>> getAllPosts(HttpServletRequest request){
        return postService.getAllPosts(request);
    }
}
