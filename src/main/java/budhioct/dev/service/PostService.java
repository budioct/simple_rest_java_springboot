package budhioct.dev.service;

import budhioct.dev.dto.PostDTO;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface PostService {

    Page<PostDTO.PostResponseDTO> getPosts(Map<String, Object> filter);
    PostDTO.PostResponseDTO createPost(PostDTO.PostRequestDTO request);
    PostDTO.PostResponseDetailDTO detailPost(PostDTO.PostRequestDetailDTO request);
    PostDTO.PostResponseDTO updatePost(PostDTO.PostRequestUpdatetDTO request);
    void remove(PostDTO.PostRequestDetailDTO request);

}
