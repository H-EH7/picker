package picker.picker_backend.post.redis;

import org.springframework.http.ResponseEntity;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostResponseDTO;

public interface PostRedisService {
    ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> getPostStatus(String tempId, String eventType);
    ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> getReplyStatus(String tempId, String eventType);

}
