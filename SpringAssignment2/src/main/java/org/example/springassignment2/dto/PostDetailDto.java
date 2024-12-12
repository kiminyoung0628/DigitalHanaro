package org.example.springassignment2.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class PostDetailDto {
    private Long postId;
    private String title;
    private String body;
    private String writerName;
    private List<CommentDto> comments;
    @QueryProjection
    public PostDetailDto(Long postId, String title, String body, String writerName, List<CommentDto> comments) {
        this.postId = postId;
        this.title = title;
        this.body = body;
        this.writerName = writerName;
        this.comments = comments;
    }
}

