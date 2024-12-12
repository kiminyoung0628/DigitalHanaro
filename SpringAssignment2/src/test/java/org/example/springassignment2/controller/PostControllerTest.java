package org.example.springassignment2.controller;

import jakarta.persistence.EntityManager;
import org.example.springassignment2.entity.Comment;
import org.example.springassignment2.entity.Post;
import org.example.springassignment2.entity.User;
import org.example.springassignment2.repository.CommentRepository;
import org.example.springassignment2.repository.PostRepository;
import org.example.springassignment2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local") // local 프로파일 활성화
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        // 초기 데이터가 삽입되었는지 확인
        assertThat(userRepository.findAll()).hasSize(5); // 5명의 유저
        assertThat(postRepository.findAll()).hasSize(10); // 각 유저당 2개의 글
    }

    /**
     * 1. 글 목록 확인
     */
    @Test
    void testGetAllPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].commentCount").exists());
    }

    /**
     * 2. 글의 상세보기 테스트(댓글들 보기)
     */
    @Test
    void testGetPostDetailWithComments() throws Exception {
        // 초기 데이터 준비
        Post post = postRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        // 댓글 추가
        Comment comment1 = new Comment("Comment 1", post, user);
        Comment comment2 = new Comment("Comment 2", post, user);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        em.persist(comment1);
        em.persist(comment2);
        em.flush();
        em.clear();

        // 글 상세보기 요청
        mockMvc.perform(get("/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.body").value(post.getBody()))
                .andExpect(jsonPath("$.writerName").value(post.getWriter().getName()))
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments[0].commentId").value(comment1.getId()))
                .andExpect(jsonPath("$.comments[0].body").value("Comment 1"))
                .andExpect(jsonPath("$.comments[0].writerName").value(user.getName()))
                .andExpect(jsonPath("$.comments[1].commentId").value(comment2.getId()))
                .andExpect(jsonPath("$.comments[1].body").value("Comment 2"))
                .andExpect(jsonPath("$.comments[1].writerName").value(user.getName()));
    }

    /**
     * 3. 글 작성 테스트
     */
    @Test
    void testCreatePost() throws Exception {
        User user = userRepository.findAll().get(0);

        mockMvc.perform(post("/posts")
                        .param("userId", String.valueOf(user.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Post Title\", \"body\":\"New Post Body\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Post Title"))
                .andExpect(jsonPath("$.writer.name").value(user.getName()));
        em.flush();
        em.clear();
    }

    /**
     * 4. 글 수정 테스트
     */
    @Test
    void testUpdatePost() throws Exception {
        Post post = postRepository.findAll().get(0);

        // 기존 데이터 영속화 확인
        em.persist(post);
        em.flush(); // 기존 데이터 DB에 반영

        // 업데이트 요청
        mockMvc.perform(put("/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\", \"body\":\"Updated Body\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.body").value("Updated Body"));

        em.flush(); // 업데이트 쿼리를 즉시 DB에 반영
        em.clear(); // 영속성 컨텍스트 초기화

        // 업데이트된 데이터 검증
        Post updatedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedPost.getBody()).isEqualTo("Updated Body");
    }

    /**
     * 5. 글 삭제 테스트
     */
    @Test
    void testDeletePost() throws Exception {
        Post post = postRepository.findAll().get(0);

        // 기존 데이터 영속화 확인
        em.persist(post);
        em.flush(); // 기존 데이터 DB에 반영 (DELETE 이전 상태 확인 가능)

        // 삭제 요청
        mockMvc.perform(delete("/posts/" + post.getId()))
                .andExpect(status().isNoContent());

        em.flush(); // DELETE 쿼리를 즉시 DB에 반영
        em.clear(); // 영속성 컨텍스트 초기화

        // 삭제된 데이터 검증
        assertThat(postRepository.findById(post.getId())).isEmpty();
    }

    /**
     * 6. 글의 댓글 목록 테스트
     */
    @Test
    void testGetCommentsByPost() throws Exception {
        // 댓글 테스트를 위해 댓글 데이터 추가
        Post post = postRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);
        Comment comment = new Comment("Test Comment", post, user);
        post.getComments().add(comment);
        postRepository.save(post);

        mockMvc.perform(get("/posts/" + post.getId() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].body").value("Test Comment"));
    }

    /**
     * 7. 글의 댓글 작성 테스트
     */
    @Test
    void testCreateComment() throws Exception {
        Post post = postRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        mockMvc.perform(post("/posts/" + post.getId() + "/comments")
                        .param("userId", String.valueOf(user.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"body\":\"New Comment Body\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("New Comment Body"));
        em.flush();
        em.clear();
    }

    /**
     * 8. 글의 댓글 수정 테스트
     */
    @Test
    void testUpdateComment() throws Exception {
        Post post = postRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        Comment comment = new Comment("Original Comment", post, user);
        em.persist(comment);
        em.flush();

        mockMvc.perform(put("/posts/" + post.getId() + "/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"body\":\"Updated Comment Body\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("Updated Comment Body"));

        em.flush(); // 업데이트를 데이터베이스에 반영
        em.clear(); // 영속성 컨텍스트 초기화

        // 수정된 댓글 검증
        Comment updatedComment = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        assertThat(updatedComment.getBody()).isEqualTo("Updated Comment Body");
    }

    /**
     * 9. 글의 댓글 삭제 테스트
     */

    @Test
    void testDeleteComment() throws Exception {
        Post post = postRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        // Comment 명시적 저장
        Comment comment = new Comment("Comment to Delete", post, user);
        em.persist(comment);
        post.getComments().add(comment);
        em.flush();

        mockMvc.perform(delete("/posts/" + post.getId() + "/comments/" + comment.getId()))
                .andExpect(status().isNoContent());

        // 삭제 후 확인
        em.flush();
        em.clear();
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertThat(updatedPost.getComments()).isEmpty();
    }
}
