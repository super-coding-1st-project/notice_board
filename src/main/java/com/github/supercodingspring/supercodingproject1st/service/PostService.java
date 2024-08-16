package com.github.supercodingspring.supercodingproject1st.service;

import com.github.supercodingspring.supercodingproject1st.config.security.JwtTokenProvider;
import com.github.supercodingspring.supercodingproject1st.repository.post.Post;
import com.github.supercodingspring.supercodingproject1st.repository.post.PostJpaRepository;
import com.github.supercodingspring.supercodingproject1st.repository.token.TokenJpaRepository;
import com.github.supercodingspring.supercodingproject1st.repository.user.UserJpaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final TokenJpaRepository tokenJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;

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
        String token = request.getHeader("Authorization");

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

    public ResponseEntity<Map<String, Object>> getAllPosts(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");

        List<Post> postList =  postJpaRepository.findAll();
        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("posts", postList);
        return ResponseEntity.ok(responseBody);

    }
}
