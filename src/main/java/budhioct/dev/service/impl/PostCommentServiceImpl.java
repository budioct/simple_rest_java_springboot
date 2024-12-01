package budhioct.dev.service.impl;

import budhioct.dev.common.Models;
import budhioct.dev.dto.PostCommentDTO;
import budhioct.dev.entity.PostCommentEntity;
import budhioct.dev.entity.PostEntity;
import budhioct.dev.repository.PostCommentRepository;
import budhioct.dev.repository.PostRepository;
import budhioct.dev.service.PostCommentService;
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
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired private ValidationService validation;
    @Autowired private PostCommentRepository postCommentRepository;
    @Autowired private PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<PostCommentDTO.PostCommentResponseDTO> getPostComments(Map<String, Object> filter) {
        Models<PostCommentEntity> models = new Models<>();
        Page<PostCommentEntity> postCommentPage = postCommentRepository.findAll(models.where(filter), models.pageableSort(filter, PostCommentEntity.class));
        List<PostCommentDTO.PostCommentResponseDTO> postCommentResponseList = postCommentPage.getContent().stream().map(PostCommentDTO::toPostCommentResp).toList();

        if (postCommentResponseList.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "post comment not found");
        }

        return new PageImpl<>(postCommentResponseList, postCommentPage.getPageable(), postCommentPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public PostCommentDTO.PostCommentResponseDetailDTO detailPostComment(PostCommentDTO.PostCommentRequestDetailDTO request) {
        validation.validate(request);

        PostEntity post = postRepository
                .findFirstById(request.getPost_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found"));

        PostCommentEntity post_comment = postCommentRepository
                .findFirstByPostsAndId(post, request.getPost_comment_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "post comment not found"));

        return PostCommentDTO.toPostCommentDetailResp(post_comment);
    }

}
