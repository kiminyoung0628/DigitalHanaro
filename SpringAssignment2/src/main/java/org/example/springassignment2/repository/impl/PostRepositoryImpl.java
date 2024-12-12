package org.example.springassignment2.repository.impl;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.springassignment2.dto.*;
import org.example.springassignment2.entity.QComment;
import org.example.springassignment2.entity.QPost;
import org.example.springassignment2.entity.QUser;
import org.example.springassignment2.repository.PostRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.springassignment2.entity.QComment.comment;
import static org.example.springassignment2.entity.QPost.post;
import static org.example.springassignment2.entity.QUser.user;

@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<PostWithCommentCountDto> findAllWithCommentCount() {
        return queryFactory
                .select(new QPostWithCommentCountDto(
                        post.id,
                        post.title,
                        post.writer.name,
                        comment.count()
                ))
                .from(post)
                .leftJoin(post.comments, comment)
                .join(post.writer, user)
                .groupBy(post.id, post.title, post.writer.name)
                .fetch();
    }




}

