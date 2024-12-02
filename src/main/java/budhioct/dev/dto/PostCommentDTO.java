package budhioct.dev.dto;

import budhioct.dev.entity.PostCommentEntity;
import budhioct.dev.entity.PostEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostCommentResponseDetailDTO {
        private long id;
        private PostResponseDTO post;
        private String review;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostResponseDTO {
        private long id;
        private String title;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostCommentRequestDetailDTO {
        @NotNull private Long post_comment_id;
        @NotNull private Long post_id;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostCommentRequestDTO {
        @JsonIgnore private Long post_id;
        @NotBlank private String review;
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

    public static PostCommentResponseDetailDTO toPostCommentDetailResp(PostCommentEntity postComment) {
        return PostCommentResponseDetailDTO.builder()
                .id(postComment.getId())
                .post(toPostResp(postComment.getPosts()))
                .review(postComment.getReview())
                .createdAt(postComment.getCreatedAt())
                .updatedAt(postComment.getUpdatedAt())
                .build();
    }

    public static PostResponseDTO toPostResp(PostEntity post) {
        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
