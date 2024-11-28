package budhioct.dev.dto;

import budhioct.dev.entity.PostEntity;
import lombok.*;

import java.time.LocalDateTime;

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

    public static PostResponseDTO toRespPost(PostEntity post){
        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
