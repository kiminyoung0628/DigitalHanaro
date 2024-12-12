package org.example.springassignment2.repository;

import org.example.springassignment2.dto.CommentDto;
import org.example.springassignment2.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom{
    List<CommentDto> findByPostId(Long postId);
}
