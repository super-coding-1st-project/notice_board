package com.github.supercodingspring.supercodingproject1st.web.controller;

import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import com.github.supercodingspring.supercodingproject1st.service.PostService;
import com.github.supercodingspring.supercodingproject1st.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.HashMap;
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

    @Operation(summary = "클라이언트로부터 요청받아 모든 게시글 조회")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 불러왔습니다."
    )
    //  TODO 겟 올 포스트 ( 기본동작 확인 완료. )
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(HttpServletRequest request){
        Map<String, Object> responseBody = new HashMap<>();

        List<PostDto> postList = postService.getAllPosts(request);
        responseBody.put("posts", postList);

        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "클라이언트로부터 요청받아 게시글 DB에 저장")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 저장했습니다."
    )
    // TODO 글쓰기 ( MySql 테이블에 저장 확인 완료. )
    @PostMapping
    public ResponseEntity<Map<String, String>> savePost(@RequestBody Post post, HttpServletRequest request){
        Map<String, String> responseBody = new HashMap<>();

        try {
            postService.savePost(post, request);
            responseBody.put("message", "성공적으로 저장하였습니다.");
            return ResponseEntity.ok(responseBody);
        }catch (Exception e){
            log.error(e.getMessage());
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }

    // TODO 게시글 ID 검색 (id 검색. USER ID 아님.  + User Id 가 아니라 게시글 ID 라 본다면 틀린부분은 아니라고 보임. )
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id, HttpServletRequest request){
        Map<String, String> responseBody = new HashMap<>();
        log.info("getPostById request received");
        try {
            PostDto postDto = postService.getPostById(id, request);
            return ResponseEntity.ok(postDto);
        }catch (Exception e){
            log.error(e.getMessage());
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }
    // 작성 글 수정.
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updatePost(@PathVariable Long id, @RequestBody PostRequest updatePostRequest){
        Map<String, String> responseBody = new HashMap<>();
        log.info("updatePost request received");
        try {
            Post updatedPost = postService.updatePost(id, updatePostRequest);
            responseBody.put("message","성공적으로 수정했습니다.");
            responseBody.put("title", updatedPost.getTitle());
            responseBody.put("content", updatedPost.getContent());
            return ResponseEntity.ok(responseBody);
        }catch (EntityNotFoundException e){
            log.error(e.getMessage());
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }catch (AuthenticationException e){
            log.error(e.getMessage());
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }catch (Exception e){
            log.error(e.getMessage());
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable Long id, @RequestBody DeletePostRequest deletePostRequest){
        Map<String, String> responseBody = new HashMap<>();
        log.info("deletePost request received");
        try {
            postService.deletePost(id, deletePostRequest);
            responseBody.put("message", "성공적으로 삭제했습니다.");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getAllPostsByEmail(@RequestParam("author_email") String email, HttpServletRequest request){
        Map<String, Object> responseBody = new HashMap<>();
        log.info("searchPosts request received");
        try {
            List<PostDto> postDtoList = postService.getAllPostsByEmail(email,request);
            responseBody.put("posts", postDtoList);
            return ResponseEntity.ok(responseBody);
        }catch (Exception e){
            log.error(e.getMessage());
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }

    // 좋아요 기능
    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> likePost(@RequestBody LikeRequest likeRequest,@PathVariable Long postId) {
        Map<String, Object> responseBody = new HashMap<>();
        log.info("likePost request received");
        PostDto postDto =  postService.likePost(likeRequest,postId);
        responseBody.put("message",postDto.getLiked()?"좋아요를 눌렀습니다.":"좋아요를 취소했습니다.");
        responseBody.put("liked",postDto.getLiked());
        responseBody.put("likeCount",postDto.getLikeCount());
        return ResponseEntity.ok(responseBody);
    }
}
