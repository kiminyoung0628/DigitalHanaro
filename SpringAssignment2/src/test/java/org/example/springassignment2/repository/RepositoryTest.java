package org.example.springassignment2.repository;

import jakarta.persistence.EntityManager;
import org.example.springassignment2.dto.CommentDto;
import org.example.springassignment2.entity.Comment;
import org.example.springassignment2.entity.Post;
import org.example.springassignment2.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local") // local 프로파일 활성화
@Transactional
class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager em;

    @Test
    void testFindAllPosts() {
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(10);
    }

    @Test
    void testFindPostById() {
        Post post = postRepository.findAll().get(0);
        Post foundPost = postRepository.findById(post.getId()).orElse(null);
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void testCreatePost() {
        User user = userRepository.findAll().get(0);
        Post newPost = new Post("New Post", "New Body", user);
        Post savedPost = postRepository.save(newPost);

        em.flush(); // 변경 사항 반영
        em.clear();

        Post foundPost = postRepository.findById(savedPost.getId()).orElse(null);
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getTitle()).isEqualTo("New Post");
    }

    @Test
    void testUpdatePost() {
        Post post = postRepository.findAll().get(0);
        post.setTitle("Updated Title");
        post.setBody("Updated Body");

        Post updatedPost = postRepository.save(post);

        em.flush(); // 변경 사항 반영
        em.clear();

        Post foundPost = postRepository.findById(updatedPost.getId()).orElse(null);
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getTitle()).isEqualTo("Updated Title");
        assertThat(foundPost.getBody()).isEqualTo("Updated Body");
    }

    @Test
    void testDeletePost() {
        Post post = postRepository.findAll().get(0);
        postRepository.delete(post);

        em.flush(); // 삭제 반영
        em.clear();

        assertThat(postRepository.findById(post.getId())).isEmpty();
    }

    @Test
    void testFindCommentsByPostId() {
        Post post = postRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        Comment comment1 = new Comment("Comment 1", post, user);
        Comment comment2 = new Comment("Comment 2", post, user);
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        em.flush(); // 댓글 저장 반영
        em.clear();

        List<CommentDto> comments = commentRepository.findByPostId(post.getId());
        assertThat(comments).hasSize(2);
    }

    @Test
    void testCreateComment() {
        Post post = postRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        Comment newComment = new Comment("New Comment", post, user);
        Comment savedComment = commentRepository.save(newComment);

        em.flush(); // 변경 사항 반영
        em.clear();

        Comment foundComment = commentRepository.findById(savedComment.getId()).orElse(null);
        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getBody()).isEqualTo("New Comment");
    }

    @Test
    void testUpdateComment() {
        Post post = postRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        Comment comment = new Comment("Original Comment", post, user);
        commentRepository.save(comment);

        em.flush(); // 초기 저장 반영
        em.clear();

        Comment savedComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(savedComment).isNotNull();

        savedComment.setBody("Updated Comment");
        commentRepository.save(savedComment);

        em.flush(); // 업데이트 반영
        em.clear();

        Comment updatedComment = commentRepository.findById(savedComment.getId()).orElse(null);
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getBody()).isEqualTo("Updated Comment");
    }

    @Test
    void testDeleteComment() {
        Post post = postRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        Comment comment = new Comment("Comment to Delete", post, user);
        commentRepository.save(comment);

        em.flush(); // 초기 저장 반영
        em.clear();

        commentRepository.delete(comment);

        em.flush(); // 삭제 반영
        em.clear();

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }
}
