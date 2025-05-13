package picker.picker_backend.post.service;

import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.*;

public interface PostClientService {

    PostApiResponseWrapper<PostResponseDTO> insertPost(PostInsertRequestDTO postInsertRequestDTO);
    PostApiResponseWrapper<PostResponseDTO> updatePost(PostUpdateRequestDTO postUpdateRequestDTO);
    PostApiResponseWrapper<PostResponseDTO> deletePost(PostDeleteRequestDTO postDeleteRequestDTO);
}
