package budhioct.dev.restcontroller;

import budhioct.dev.dto.PostDTO;
import budhioct.dev.service.PostService;
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
@RequestMapping(path = "/post")
public class PostRestController {

    @Autowired private PostService postService;

    @GetMapping(
            path = "/fetch",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RestResponse.list<List<PostDTO.PostResponseDTO>> getPosts(@RequestBody Map<String, Object> filter){
        Page<PostDTO.PostResponseDTO> postResponse = postService.getPosts(filter);
        return RestResponse.list.<List<PostDTO.PostResponseDTO>>builder()
                .list(postResponse.getContent())
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .paging(RestResponse.restPagingResponse.builder()
                        .currentPage(postResponse.getNumber())
                        .totalPage(postResponse.getTotalPages())
                        .sizePage(postResponse.getSize())
                        .build())
                .build();
    }

}
