package org.example.springassignment2.repository;

import org.example.springassignment2.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom, QuerydslPredicateExecutor<Comment> {
}
