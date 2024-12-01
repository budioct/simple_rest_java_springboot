package budhioct.dev.service;

import budhioct.dev.dto.PostCommentDTO;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface PostCommentService {
    Page<PostCommentDTO.PostCommentResponseDTO> getPostComments(Map<String, Object> filter);
    PostCommentDTO.PostCommentResponseDetailDTO detailPostComment(PostCommentDTO.PostCommentRequestDetailDTO request);
}
