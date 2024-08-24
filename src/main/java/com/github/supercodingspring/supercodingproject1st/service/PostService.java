package com.github.supercodingspring.supercodingproject1st.service;

import com.github.supercodingspring.supercodingproject1st.repository.UserLikesRepository;
import com.github.supercodingspring.supercodingproject1st.repository.entity.Post;
import com.github.supercodingspring.supercodingproject1st.repository.entity.User;
import com.github.supercodingspring.supercodingproject1st.repository.entity.UserLikes;
import com.github.supercodingspring.supercodingproject1st.repository.post.PostRepository;
import com.github.supercodingspring.supercodingproject1st.repository.user.UserRepository;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotFoundException;
import com.github.supercodingspring.supercodingproject1st.service.mapper.PostMapper;
import com.github.supercodingspring.supercodingproject1st.web.dto.DeletePostRequest;
import com.github.supercodingspring.supercodingproject1st.web.dto.LikeRequest;
import com.github.supercodingspring.supercodingproject1st.web.dto.PostDto;
import com.github.supercodingspring.supercodingproject1st.web.dto.PostRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Service
@RequiredArgsConstructor
public class PostService {
    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private final UserLikesRepository userLikesRepository;

    @Value("${jwt.secret-key-source}")
    private String secretKeySource;
    private String secretKey;

    @PostConstruct
    public void setUp(){ //
        secretKey = Base64.getEncoder()
                .encodeToString(secretKeySource.getBytes());
    }

    public ResponseEntity<Map<String, Object>> getAllPosts(HttpServletRequest request) {
        String userEmail = request.getHeader("Email");
        User currentUser = userRepository.findByEmail(userEmail);

        List<PostDto> postList =  postRepository.findAll().stream()
                .map(post->{
                        PostDto postDto = PostMapper.INSTANCE.PosttoPostDto(post);
                        Boolean isLiked = userLikesRepository.existsByUserAndPost(currentUser, post);
                        postDto.setLiked(isLiked);
                        return postDto;
                }).toList();

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
        try{
            User user = userRepository.findUserByUserNameFetchJoin(userName) //토큰에서 가져온 userName으로 repository에서 user 객체 탐색
                    .orElseThrow(()->new NotFoundException("User not found"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            Post savePost = Post.builder() //빌더 패턴 사용
                    .title(post.getTitle()) //사용자가 입력한 제목
                    .content(post.getContent()) //사용자가 입력한 내용
                    .createdAt(LocalDateTime.now().format(formatter)) //작성일시를 현재, 위의 formatter 형식으로
                    .user(user) //토큰을 파싱해서 가져온 user
                    .likeCount(0) //처음 좋아요 개수 0
                    .build();

            postRepository.save(savePost); //JPA를 이용하여 DB에 저장
            responseBody.put("message", "성공적으로 저장하였습니다."); //클라이언트 응답 메세지
            return ResponseEntity.ok(responseBody);
        }catch (NotFoundException e ){
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }

    public ResponseEntity<PostDto> getPostById(Long id, HttpServletRequest request) {
        Optional<Post> post = postRepository.findById(id);
        PostDto postDto = PostMapper.INSTANCE.PosttoPostDto(post.get());
        User user = userRepository.findByEmail(request.getHeader("Email"));
        if(user.getUserLikes().stream().map(UserLikes::getPost).toList().contains(post))
            postDto.setLiked(true);
        else
            postDto.setLiked(false);

        return ResponseEntity.ok(postDto);
    }

    public ResponseEntity<Map<String,String>> updatePost(Long id, PostRequest updatedPostRequest) {
        Map<String, String> responseBody = new HashMap<>();
        log.info(updatedPostRequest.getTitle()+" "+updatedPostRequest.getContent());
        Post requestPost = postRepository.findById(id).get();
        if(requestPost.getUser() == userRepository.findByEmail(updatedPostRequest.getEmail())) {
            if (postRepository.findById(id).isPresent()) {
                requestPost.setTitle(updatedPostRequest.getTitle());
                requestPost.setContent(updatedPostRequest.getContent());
                requestPost.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                postRepository.save(requestPost);

                responseBody.put("title", updatedPostRequest.getTitle());
                responseBody.put("content", updatedPostRequest.getContent());

                return ResponseEntity.ok(responseBody);
            } else {
                responseBody.put("message", "수정에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }
        }else {
            responseBody.put("message","권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
    }

    public ResponseEntity<Map<String, String>> deletePost(Long id, DeletePostRequest deletedPostRequest) {
        Map<String, String> responseBody = new HashMap<>();
        Post requestPost = postRepository.findById(id).get();
        if(requestPost.getUser() == userRepository.findByEmail(deletedPostRequest.getEmail())) {
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
        else {
            responseBody.put("message","권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
        }
    }

    public ResponseEntity<Map<String, Object>> getAllPostsByEmail(String email, HttpServletRequest request) {
        Map<String, Object> responseBody = new HashMap<>();
        List<Post> posts = postRepository.findAllByUser_Email(email);
        List<PostDto> postDto = posts.stream().map(PostMapper.INSTANCE::PosttoPostDto).toList();
        responseBody.put("posts", postDto);
        return ResponseEntity.ok(responseBody);
    }

    @Transactional
    public ResponseEntity<Map<String,Object>> likePost(LikeRequest likeRequest, Long postId) {
        Map<String, Object> responseBody = new HashMap<>();

        try{
            User user = userRepository.findByEmail(likeRequest.getEmail());
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found with id " + postId));
            log.info(user.toString(),post.toString());

            List<Post> posts = user.getUserLikes().stream().map(UserLikes::getPost).toList();
            Boolean isLiked = posts.contains(post);

            if(isLiked) {
                postRepository.decrementLikeCount(post.getId());
                UserLikes userLikes = userLikesRepository.findByUserAndPost(user, post);
                userLikesRepository.delete(userLikes);
                user.getUserLikes().remove(userLikes);
                responseBody.put("liked",false);
                isLiked = false;
            }else {
                postRepository.incrementLikeCount(post.getId());
                UserLikes userLikes = userLikesRepository.save(new UserLikes(user,post));
                user.getUserLikes().add(userLikes);
                responseBody.put("liked",true);
                isLiked = true;
            }
            postRepository.flush();
            userRepository.flush();

            entityManager.clear();

            post = postRepository.findById(postId).get();
            log.info(post.getLikeCount().toString());

            responseBody.put("message", isLiked ? "좋아요를 눌렀습니다.":"좋아요를 취소했습니다.");
            responseBody.put("likeCount", post.getLikeCount());
            log.info(user.getUserLikes().toString(), post);
            log.info(post.getLikeCount().toString());

            return ResponseEntity.ok(responseBody);
        }catch (EntityNotFoundException e){
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

    }

}
