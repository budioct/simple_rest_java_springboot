package budhioct.dev.restcontroller;

import budhioct.dev.dto.PostCommentDTO;
import budhioct.dev.service.PostCommentService;
import budhioct.dev.utilities.Constants;
import budhioct.dev.utilities.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post-comment")
public class PostCommentRestController {

    @Autowired private PostCommentService postCommentService;

    @GetMapping(
            path = "/fetch",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
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

}
