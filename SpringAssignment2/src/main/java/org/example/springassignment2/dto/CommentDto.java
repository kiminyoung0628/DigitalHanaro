package org.example.springassignment2.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private Long commentId;
    private String body;
    private String writerName;
    @QueryProjection
    public CommentDto(Long commentId, String body, String writerName) {
        this.commentId = commentId;
        this.body = body;
        this.writerName = writerName;
    }
}
