package budhioct.dev.dto;

import budhioct.dev.entity.PostCommentEntity;
import lombok.*;

import java.time.LocalDateTime;

public class PostCommentDTO {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostCommentResponseDTO {
        private long id;
        private long post_id;
        private String review;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    public static PostCommentResponseDTO toPostCommentResp(PostCommentEntity postComment) {
        return PostCommentResponseDTO.builder()
                .id(postComment.getId())
                .post_id(postComment.getPosts().getId())
                .review(postComment.getReview())
                .createdAt(postComment.getCreatedAt())
                .updatedAt(postComment.getUpdatedAt())
                .build();
    }

}
