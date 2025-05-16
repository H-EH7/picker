package picker.picker_backend.post.likes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.likes.model.dto.LikesDeleteRequestDTO;
import picker.picker_backend.post.likes.model.dto.LikesInsertRequestDTO;
import picker.picker_backend.post.likes.model.dto.LikesSelectDTO;
import picker.picker_backend.post.likes.service.LikesRedisService;

@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikesController {


    private final LikesRedisService likesRedisService;

    @GetMapping
    public ResponseEntity<PostApiResponseWrapper<LikesSelectDTO>> getLikes(@PathVariable Long postId){

        return null;
    }

    @PostMapping("/insert")
    public ResponseEntity<PostApiResponseWrapper<LikesInsertRequestDTO>> insertLikes(@PathVariable Long postId, @RequestParam String userId){

        return likesRedisService.insertLikes(LikesInsertRequestDTO.builder()
                .postId(postId)
                .userId(userId)
                .build()
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<PostApiResponseWrapper<LikesDeleteRequestDTO>> deleteLikes(@PathVariable Long postId, @RequestParam String userId){

        return likesRedisService.deleteLikes(LikesDeleteRequestDTO.builder()
                .postId(postId)
                .userId(userId)
                .build()
        );
    }

}
