package picker.picker_backend.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.*;
import picker.picker_backend.post.redis.PostRedisService;
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

    @Autowired
    private PostRedisService postRedisService;

    @GetMapping("/{userId}")
    public PostApiResponseWrapper<List<PostSelectDTO>> getPostById(@PathVariable String userId){
        return postQueryService.getPostById(userId);
    }

    @GetMapping("/status/{tempId}")
    public PostApiResponseWrapper<PostResponseDTO> getPostStatus(@PathVariable String tempId,
                                                                 @PathVariable String eventType){
        return postRedisService.getPostStatus(tempId,eventType);
    }


    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostApiResponseWrapper<PostResponseDTO> insertPost(@RequestParam String userId,
                                                              @RequestParam String postText,
                                                              @RequestParam String tempId){
        return postClientService.insertPost(
                PostInsertRequestDTO.builder()
                        .userId(userId)
                        .postText(postText)
                        .tempId(tempId)
                        .build()
        );
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostApiResponseWrapper<PostResponseDTO> updatePost(@RequestParam String userId,
                                                              @RequestParam String postText,
                                                              @RequestParam long postId,
                                                              @RequestParam String tempId){
        return postClientService.updatePost(
                PostUpdateRequestDTO.builder()
                        .userId(userId)
                        .postText(postText)
                        .postId(postId)
                        .tempId(tempId)
                        .build()
        );
    }

    @PutMapping(value = "/delete")
    public PostApiResponseWrapper<PostResponseDTO> deletePost(@RequestParam String userId,
                                                              @RequestParam long postId,
                                                              @RequestParam String tempId){
        return postClientService.deletePost(
                PostDeleteRequestDTO.builder()
                .userId(userId)
                .postId(postId)
                .tempId(tempId)
                .build()
        );
    }


}
