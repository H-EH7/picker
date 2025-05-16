package picker.picker_backend.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.likes.service.LikesRedisService;
import picker.picker_backend.post.model.dto.*;
import picker.picker_backend.post.redis.PostRedisService;
import picker.picker_backend.post.reply.model.dto.ReplySelectDTO;
import picker.picker_backend.post.reply.service.ReplyQueryService;
import picker.picker_backend.post.service.PostClientService;
import picker.picker_backend.post.service.PostQueryService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private PostClientService postClientService;

    @Autowired
    private PostQueryService postQueryService;

    @Autowired
    private PostRedisService postRedisService;

    @Autowired
    private ReplyQueryService replyQueryService;

    @Autowired
    private LikesRedisService likesRedisService;

    @GetMapping
    public ResponseEntity<PostApiResponseWrapper<List<PostSelectDTO>>> getPostLists(){

        return postQueryService.getPostLists();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostApiResponseWrapper<PostWithResponseDTO>> getPost(@PathVariable long postId){

        PostSelectDTO postData = postQueryService.getPost(postId);

        if(postData != null){

            List<ReplySelectDTO> replyData = replyQueryService.getPostWithResponse(postId);

            long likesCount = likesRedisService.getLikesCount(postId);

            PostWithResponseDTO postWithResponseDTO = new PostWithResponseDTO(
                    postData,
                    replyData,
                    likesCount
            );

            return ResponseEntity.ok(PostApiResponseWrapper.success(postWithResponseDTO, "Post Select Success"));

        }else{

            return ResponseEntity.status(404).body(PostApiResponseWrapper.fail(null, "Post Select Failed"));
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<PostApiResponseWrapper<List<PostSelectDTO>>> getPostById(@PathVariable String userId){
        return postQueryService.getPostById(userId);
    }

    @GetMapping("/status")
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> getPostStatus(@RequestParam String tempId,
                                                                 @RequestParam String eventType){
        return postRedisService.getPostStatus(tempId,eventType);
    }


    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> insertPost(@RequestParam String userId,
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
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> updatePost(@RequestParam String userId,
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
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> deletePost(@RequestParam String userId,
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
