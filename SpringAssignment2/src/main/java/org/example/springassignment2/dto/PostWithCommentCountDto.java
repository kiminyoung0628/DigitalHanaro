package org.example.springassignment2.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PostWithCommentCountDto {
    private Long postId;
    private String title;
    private String writerName;
    private Long commentCount;
    @QueryProjection
    public PostWithCommentCountDto(Long postId, String title, String writerName, Long commentCount) {
        this.postId = postId;
        this.title = title;
        this.writerName = writerName;
        this.commentCount = commentCount;
    }
}
