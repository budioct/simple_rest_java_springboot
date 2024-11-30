package budhioct.dev.service.impl;

import budhioct.dev.common.Models;
import budhioct.dev.dto.PostCommentDTO;
import budhioct.dev.entity.PostCommentEntity;
import budhioct.dev.repository.PostCommentRepository;
import budhioct.dev.service.PostCommentService;
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

    @Autowired private PostCommentRepository postCommentRepository;

    @Transactional(readOnly = true)
    public Page<PostCommentDTO.PostCommentResponseDTO> getPostComments(Map<String, Object> filter) {
        Models<PostCommentEntity> models = new Models<>();
        Page<PostCommentEntity> postCommentPage = postCommentRepository.findAll(models.where(filter), models.pageableSort(filter));
        List<PostCommentDTO.PostCommentResponseDTO> postCommentResponseList = postCommentPage.getContent().stream().map(PostCommentDTO::toPostCommentResp).toList();

        if (postCommentResponseList.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "post comment not found");
        }

        return new PageImpl<>(postCommentResponseList, postCommentPage.getPageable(), postCommentPage.getTotalPages());
    }

}
