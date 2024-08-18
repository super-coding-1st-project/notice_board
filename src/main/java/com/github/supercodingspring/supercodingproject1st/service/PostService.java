package com.github.supercodingspring.supercodingproject1st.service;

import com.github.supercodingspring.supercodingproject1st.config.security.JwtTokenProvider;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.repository.post.PostRepository;
import com.github.supercodingspring.supercodingproject1st.repository.token.TokenRepository;
import com.github.supercodingspring.supercodingproject1st.repository.user.UserRepository;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotFoundException;
import com.github.supercodingspring.supercodingproject1st.service.mapper.PostMapper;
import com.github.supercodingspring.supercodingproject1st.web.dto.PostDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.PostRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.secret-key-source}")
    private String secretKeySource;
    private String secretKey;

    @PostConstruct
    public void setUp(){ //
        secretKey = Base64.getEncoder()
                .encodeToString(secretKeySource.getBytes());
    }

    public ResponseEntity<Map<String, Object>> getAllPosts(HttpServletRequest request) {

        List<PostDto> postList =  postRepository.findAll().stream().map(PostMapper.INSTANCE::PosttoPostDto).toList();
        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("posts", postList);
        return ResponseEntity.ok(responseBody);

    }

    public ResponseEntity<Map<String, String>> savePost(Post post, HttpServletRequest request) {
        //JWT TOKEN에서 user_name을 받아오기 위한 코드 추가 -> 로그인 기능과 관련
        String token = request.getHeader("Authorization");
        Map<String, String> responseBody = new HashMap<>();

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey).parseClaimsJws(token).getBody(); //헤더에 포함된 토큰 파싱 과정

        String userName = claims.get("user_name").toString(); //파싱한 토큰에서 user_name을 가진 value를 가져와 userName 변수에 저장

        User user = userRepository.findUserByUserNameFetchJoin(userName) //토큰에서 가져온 userName으로 repository에서 user 객체 탐색
                .orElseThrow(()->new NotFoundException("User not found"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Post savePost = Post.builder() //빌더 패턴 사용
                .title(post.getTitle()) //사용자가 입력한 제목
                .content(post.getContent()) //사용자가 입력한 내용
                .createdAt(LocalDateTime.now().format(formatter)) //작성일시를 현재, 위의 formatter 형식으로
                .user(user) //토큰을 파싱해서 가져온 userName
                .build();

        postRepository.save(savePost); //JPA를 이용하여 DB에 저장
        responseBody.put("message", "성공적으로 저장하였습니다."); //클라이언트 응답 메세지
        return ResponseEntity.ok(responseBody);
    }

    public ResponseEntity<PostDto> getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        PostDto postDto = PostMapper.INSTANCE.PosttoPostDto(post.get());
        return ResponseEntity.ok(postDto);
    }

    public ResponseEntity<Map<String,String>> updatePost(Long id, PostRequest updatedPostRequest) {
        Map<String, String> responseBody = new HashMap<>();
        log.info(updatedPostRequest.getTitle()+" "+updatedPostRequest.getContent());
        if(postRepository.findById(id).isPresent()){
            Post requestPost = postRepository.findById(id).get();
            requestPost.setTitle(updatedPostRequest.getTitle());
            requestPost.setContent(updatedPostRequest.getContent());
            requestPost.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            postRepository.save(requestPost);

            responseBody.put("title",updatedPostRequest.getTitle());
            responseBody.put("content",updatedPostRequest.getContent());

            return ResponseEntity.ok(responseBody);
        }
        else {
            responseBody.put("message","수정에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }
    }

    public ResponseEntity<Map<String, String>> deletePost(Long id) {
        Map<String, String> responseBody = new HashMap<>();
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            responseBody.put("message","성공적으로 삭제했습니다.");
            return ResponseEntity.ok(responseBody);
            // 204 No Content -> 200 ok에 성공적으로 삭제했다는 메제지를 담아 클라이언트에 응답하는 것이 낫다고 판단하여 수정하였습니다. (장태영)
        } else {
            responseBody.put("message","삭제에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            // 404 Not Found
        }
    }

    public ResponseEntity<Map<String, Object>> getAllPostsByEmail(String email, HttpServletRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        List<Post> posts = postRepository.findAllByUser_Email(email);
        List<PostDto> postDto = posts.stream().map(PostMapper.INSTANCE::PosttoPostDto).toList();
        responseBody.put("posts", postDto);
        return ResponseEntity.ok(responseBody);
    }
}
