package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import com.github.supercodingspring.supercodingproject1st.service.PostService;
import com.github.supercodingspring.supercodingproject1st.web.dto.PostDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.PostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
=======
import java.util.HashMap;
>>>>>>> develop_great
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
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
    // TODO 글쓰기 ( MySql 테이블에 저장 확인 완료. )
    @PostMapping
    public ResponseEntity<Map<String, String>> savePost(@RequestBody Post post, HttpServletRequest request){
        return postService.savePost(post, request);
    }

    @Operation(summary = "클라이언트로부터 요청받아 모든 게시글 조회")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 불러왔습니다."
    )
    //  TODO 겟 올 포스트 ( 기본동작 확인 완료. )
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(HttpServletRequest request){
        return postService.getAllPosts(request);
    }

    // TODO 게시글 ID 검색 (id 검색. USER ID 아님.  + User Id 가 아니라 게시글 ID 라 본다면 틀린부분은 아니라고 보임. )
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id){
        log.info("getPostById request received");
        return postService.getPostById(id);
    }
    // 작성 글 수정.
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updatePost(@PathVariable Long id, @RequestBody PostRequest updatePostRequest){
        log.info("updatePost request received");
        return postService.updatePost(id, updatePostRequest);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable Long id){
        log.info("deletePost request received");
        return postService.deletePost(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchPosts(@RequestParam("author_email") String email, HttpServletRequest request){
        log.info("searchPosts request received");
        return postService.getAllPostsByEmail(email,request);
    }
<<<<<<< HEAD
=======

    // 좋아요 기능
    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, String>> likePost(@PathVariable Long postId, HttpServletRequest request) {
        postService.likePost(postId);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Post liked successfully");
        return ResponseEntity.ok(responseBody);
    }

    // 싫어요 기능
    @PostMapping("/{postId}/dislike")
    public ResponseEntity<Map<String, String>> dislikePost(@PathVariable Long postId, HttpServletRequest request) {
        postService.dislikePost(postId);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Post disliked successfully");
        return ResponseEntity.ok(responseBody);
    }
>>>>>>> develop_great
}
