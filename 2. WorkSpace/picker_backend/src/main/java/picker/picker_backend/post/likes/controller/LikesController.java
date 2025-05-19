package picker.picker_backend.post.likes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.likes.model.dto.LikesDeleteRequestDTO;
import picker.picker_backend.post.likes.model.dto.LikesInsertRequestDTO;
import picker.picker_backend.post.likes.model.dto.LikesSelectDTO;
import picker.picker_backend.post.likes.service.LikesClientService;
import picker.picker_backend.post.likes.service.LikesQueryService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class LikesController {

    private final LikesQueryService likesQueryService;
    private final LikesClientService likesClientService;

    @GetMapping(path = "/{userId}/likes/users")
    public ResponseEntity<PostApiResponseWrapper<List<LikesSelectDTO>>> getLikesByUserId(@PathVariable String userId){

        return likesQueryService.getLikesByUserId(userId);
    }

    @PostMapping("/{postId}/likes/insert")
    public ResponseEntity<PostApiResponseWrapper<LikesInsertRequestDTO>> insertLikes(@PathVariable Long postId, @RequestParam String userId){

        return likesClientService.insertLikes(LikesInsertRequestDTO.builder()
                .postId(postId)
                .userId(userId)
                .build()
        );
    }

    @DeleteMapping("/{postId}/likes/delete")
    public ResponseEntity<PostApiResponseWrapper<LikesDeleteRequestDTO>> deleteLikes(@PathVariable Long postId, @RequestParam String userId){

        return likesClientService.deleteLikes(LikesDeleteRequestDTO.builder()
                .postId(postId)
                .userId(userId)
                .build()
        );
    }

}
