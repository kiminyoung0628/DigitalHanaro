package org.example.springassignment2.controller;

import lombok.RequiredArgsConstructor;
import org.example.springassignment2.dto.*;
import org.example.springassignment2.entity.*;
import org.example.springassignment2.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // 1. 글 목록 조회
    @GetMapping
    public ResponseEntity<List<PostWithCommentCountDto>> getAllPosts() {
        List<PostWithCommentCountDto> posts = postRepository.findAllWithCommentCount();
        return ResponseEntity.ok(posts);
    }

    // 2. 글 상세보기
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailDto> getPostDetail(@PathVariable Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<CommentDto> comments = commentRepository.findByPostId(postId).stream()
                .map(comment -> new CommentDto(
                        comment.getCommentId(),
                        comment.getBody(),
                        comment.getWriterName()
                ))
                .collect(Collectors.toList());

        PostDetailDto postDetail = new PostDetailDto(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getWriter().getName(),
                comments
        );

        return ResponseEntity.ok(postDetail);
    }

    // 3. 글 작성
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestParam String userId, @RequestBody Post post) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setWriter(writer);
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }

    // 4. 글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setBody(updatedPost.getBody());
        Post savedPost = postRepository.save(existingPost);
        return ResponseEntity.ok(savedPost);
    }

    // 5. 글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
        return ResponseEntity.noContent().build();
    }

    // 6. 댓글 목록 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDto> comments = commentRepository.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 7. 댓글 작성
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> createComment(
            @PathVariable Long postId,
            @RequestParam String userId,
            @RequestBody Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        comment.setPost(post);
        comment.setWriter(writer);
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(savedComment);
    }

    // 8. 댓글 수정
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long commentId,
            @RequestBody Comment updatedComment) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        existingComment.setBody(updatedComment.getBody());
        Comment savedComment = commentRepository.save(existingComment);
        return ResponseEntity.ok(savedComment);
    }

    // 9. 댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentRepository.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }
}
