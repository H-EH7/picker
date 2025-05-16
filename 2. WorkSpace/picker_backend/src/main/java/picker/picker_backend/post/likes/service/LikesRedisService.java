package picker.picker_backend.post.likes.service;

import org.springframework.http.ResponseEntity;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.likes.model.dto.LikesDeleteRequestDTO;
import picker.picker_backend.post.likes.model.dto.LikesInsertRequestDTO;

public interface LikesRedisService {
    ResponseEntity<PostApiResponseWrapper<LikesInsertRequestDTO>> insertLikes(LikesInsertRequestDTO likesInsertRequestDTO);
    ResponseEntity<PostApiResponseWrapper<LikesDeleteRequestDTO>> deleteLikes(LikesDeleteRequestDTO likesDeleteRequestDTO);
    long getLikesCount(long postId);

}
