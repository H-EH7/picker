package picker.picker_backend.post.redis;

import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostResponseDTO;

public interface PostRedisService {
    PostApiResponseWrapper<PostResponseDTO> getPostStatus(String tempId, String eventType);
}
