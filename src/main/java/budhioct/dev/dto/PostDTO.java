package budhioct.dev.dto;

import budhioct.dev.entity.PostCommentEntity;
import budhioct.dev.entity.PostEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PostDTO {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostResponseDTO{
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
    public static class PostResponseDetailDTO{
        private long id;
        private String title;
        private List<PostCommentResponseDTO> post_comments;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostCommentResponseDTO {
        private long id;
        private String review;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostRequestDetailDTO{
        @NotNull private long id;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostRequestDTO{
        @NotBlank private String title;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostRequestUpdatetDTO{
        @JsonIgnore private long id;
        @NotBlank private String title;
    }

    public static PostResponseDTO toPostResp(PostEntity post){
        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostResponseDetailDTO toPostDetailResp(PostEntity post){
        return PostResponseDetailDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .post_comments(post.getPost_comments().stream().map(PostDTO::toPostCommentResp).toList())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostCommentResponseDTO toPostCommentResp(PostCommentEntity postComment) {
        return PostCommentResponseDTO.builder()
                .id(postComment.getId())
                .review(postComment.getReview())
                .createdAt(postComment.getCreatedAt())
                .updatedAt(postComment.getUpdatedAt())
                .build();
    }

}
