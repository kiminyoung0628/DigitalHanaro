package org.example.springassignment2.repository;

import org.example.springassignment2.dto.PostDetailDto;
import org.example.springassignment2.dto.PostWithCommentCountDto;


import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    List<PostWithCommentCountDto> findAllWithCommentCount();
}
