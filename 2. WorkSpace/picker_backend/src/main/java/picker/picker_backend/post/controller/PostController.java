package picker.picker_backend.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picker.picker_backend.post.model.dto.PostSelectDTO;
import picker.picker_backend.post.service.PostClientService;
import picker.picker_backend.post.service.PostQueryService;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private PostClientService postClientService;

    @Autowired
    private PostQueryService postQueryService;

    @GetMapping("/{userId}")
    public List<PostSelectDTO> getPostById(@PathVariable String userId){

        return postQueryService.getPostById(userId);
    }

    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public int insertPost(@RequestParam String userId,
                          @RequestParam String postText,
                          @RequestParam(required = false) MultipartFile file){


        return 0;
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public int updatePost(@RequestParam String userId,
                          @RequestParam String postText,
                          @RequestParam long postId,
                          @RequestParam(required = false) MultipartFile file){


        return 0;
    }
}
