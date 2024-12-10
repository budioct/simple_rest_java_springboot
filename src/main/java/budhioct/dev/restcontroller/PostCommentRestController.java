package budhioct.dev.restcontroller;

import budhioct.dev.dto.PostCommentDTO;
import budhioct.dev.service.PostCommentService;
import budhioct.dev.utilities.Constants;
import budhioct.dev.utilities.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
public class PostCommentRestController {

    @Autowired private PostCommentService postCommentService;

    @GetMapping(
            path = "/post-comment/fetch",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read', 'management:read', 'admin:read')")
    public RestResponse.list<List<PostCommentDTO.PostCommentResponseDTO>> getPostComments(@RequestBody Map<String, Object> filter) {
        Page<PostCommentDTO.PostCommentResponseDTO> postCommentResponse = postCommentService.getPostComments(filter);
        return RestResponse.list.<List<PostCommentDTO.PostCommentResponseDTO>>builder()
                .list(postCommentResponse.getContent())
                .total_data(postCommentResponse.stream().toList().size())
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .paging(RestResponse.restPagingResponse.builder()
                        .currentPage(postCommentResponse.getNumber())
                        .totalPage(postCommentResponse.getTotalPages())
                        .sizePage(postCommentResponse.getSize())
                        .build())
                .build();
    }

    @GetMapping(
            path = "/post/{post_id}/post-comment/{post_comment_id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read', 'management:read', 'admin:read')")
    public RestResponse.object<PostCommentDTO.PostCommentResponseDetailDTO> detailPostComment(@PathVariable(name = "post_id") long post_id,
                                                                                              @PathVariable(name = "post_comment_id") long post_comment_id,
                                                                                              PostCommentDTO.PostCommentRequestDetailDTO request) {
        request.setPost_id(post_id);
        request.setPost_comment_id(post_comment_id);
        PostCommentDTO.PostCommentResponseDetailDTO postCommentResponse = postCommentService.detailPostComment(request);
        return RestResponse.object.<PostCommentDTO.PostCommentResponseDetailDTO>builder()
                .data(postCommentResponse)
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .build();

    }

    @PostMapping(
            path = "/post/{post_id}/post-comment/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:create', 'management:create', 'admin:create')")
    public RestResponse.object<PostCommentDTO.PostCommentResponseDTO> createPostComment(@PathVariable(name = "post_id") long post_id,
                                                                                        @RequestBody PostCommentDTO.PostCommentRequestDTO request) {
        request.setPost_id(post_id);
        PostCommentDTO.PostCommentResponseDTO postCommentResponse = postCommentService.createPostComment(request);
        return RestResponse.object.<PostCommentDTO.PostCommentResponseDTO>builder()
                .data(postCommentResponse)
                .status_code(Constants.OK)
                .message(Constants.CREATE_MESSAGE)
                .build();
    }

    @DeleteMapping(
            path = "/post/{post_id}/post-comment/{post_comment_id}/remove",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('management:delete', 'admin:delete')")
    public RestResponse.object<String> removePostComment(@PathVariable(name = "post_id") long post_id,
                                                         @PathVariable(name = "post_comment_id") long post_comment_id,
                                                         PostCommentDTO.PostCommentRequestDetailDTO request) {
        request.setPost_id(post_id);
        request.setPost_comment_id(post_comment_id);
        postCommentService.removePostComment(request);
        return RestResponse.object.<String>builder()
                .data("")
                .status_code(Constants.OK)
                .message(Constants.DELETE_MESSAGE)
                .build();
    }

}
