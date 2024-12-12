package org.example.springassignment2.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.springassignment2.dto.CommentDto;
import org.example.springassignment2.dto.QCommentDto;
import org.example.springassignment2.entity.QComment;
import org.example.springassignment2.repository.CommentRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.springassignment2.entity.QComment.comment;
import static org.example.springassignment2.entity.QUser.user;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<CommentDto> findByPostId(Long postId) {
        return queryFactory
                .select(new QCommentDto(
                        comment.id,
                        comment.body,
                        comment.writer.name
                ))
                .from(comment)
                .join(comment.writer, user)
                .where(comment.post.id.eq(postId))
                .fetch();
    }
}
