package budhioct.dev.service.impl;

import budhioct.dev.common.Models;
import budhioct.dev.dto.PostDTO;
import budhioct.dev.entity.PostEntity;
import budhioct.dev.repository.PostRepository;
import budhioct.dev.service.PostService;
import budhioct.dev.utilities.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    @Autowired private ValidationService validation;
    @Autowired private PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<PostDTO.PostResponseDTO> getPosts(Map<String, Object> filter) {
        Models<PostEntity> models = new Models<>();
        Page<PostEntity> postPage = postRepository.findAll(models.where(filter), models.pageableSort(filter, PostEntity.class));
        List<PostDTO.PostResponseDTO> postResponseList = postPage.getContent().stream().map(PostDTO::toPostResp).toList();

        if (postResponseList.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found");
        }

        return new PageImpl<>(postResponseList, postPage.getPageable(), postPage.getTotalPages());
    }

    @Transactional
    public PostDTO.PostResponseDTO createPost(PostDTO.PostRequestDTO request) {
        validation.validate(request);

        PostEntity post = new PostEntity();
        post.setTitle(request.getTitle());
        postRepository.save(post);

        return PostDTO.toPostResp(post);
    }

    @Transactional(readOnly = true)
    public PostDTO.PostResponseDTO detailPost(PostDTO.PostRequestDetailDTO request) {
        validation.validate(request);

        PostEntity post = postRepository
                .findFirstById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found"));

        return PostDTO.toPostResp(post);
    }

    @Transactional
    public PostDTO.PostResponseDTO updatePost(PostDTO.PostRequestUpdatetDTO request) {
        validation.validate(request);

        PostEntity post = postRepository
                .findFirstById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found"));

        post.setTitle(request.getTitle());
        postRepository.save(post);

        return PostDTO.toPostResp(post);
    }

    @Transactional
    public void remove(PostDTO.PostRequestDetailDTO request) {
        validation.validate(request);

        PostEntity post = postRepository.
                findFirstById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found"));

        postRepository.deleteById(post.getId());
        //postRepository.delete(post);

    }

}
