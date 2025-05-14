package picker.picker_backend.post.service;

import org.springframework.http.ResponseEntity;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.*;

public interface PostClientService {

    ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> insertPost(PostInsertRequestDTO postInsertRequestDTO);
    ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> updatePost(PostUpdateRequestDTO postUpdateRequestDTO);
    ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> deletePost(PostDeleteRequestDTO postDeleteRequestDTO);
}
