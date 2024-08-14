package com.github.supercodingspring.supercodingproject1st.service;

import com.github.supercodingspring.supercodingproject1st.repository.post.Post;
import com.github.supercodingspring.supercodingproject1st.repository.post.PostJpaRepository;
import com.github.supercodingspring.supercodingproject1st.repository.user.UserJpaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Value("${jwt.secret-key-source}")
    private String secretKeySource;
    private String secretKey;

    @PostConstruct
    public void setUp(){ //
        secretKey = Base64.getEncoder()
                .encodeToString(secretKeySource.getBytes());
    }

    public ResponseEntity<Map<String, String>> savePost(Post post, HttpServletRequest request) {

        Map<String, String> responseBody = new HashMap<>();
        String token = request.getHeader("X-AUTH-TOKEN");

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey).parseClaimsJws(token).getBody();
        String userName = claims.get("user_name").toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Post savePost = Post.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .created_at(LocalDateTime.now().format(formatter))
                .author(userName)
                .build();

        postJpaRepository.save(savePost);
        responseBody.put("message", "성공적으로 저장하였습니다.");
        return ResponseEntity.ok(responseBody);
    }

    public ResponseEntity<Map<String, List<Post>>> getAllPosts() {
        List<Post> postList =  postJpaRepository.findAll();
        Map<String, List<Post>> responseBody = new HashMap<>();
        responseBody.put("posts", postList);
        return ResponseEntity.ok(responseBody);
    }
}
