package picker.picker_backend.post.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.component.manger.PostRedisStatusManager;
import picker.picker_backend.post.factory.PostApiResponseFactory;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostResponseDTO;
import picker.picker_backend.post.postenum.PostEventType;
import picker.picker_backend.post.postenum.PostStatus;

@Service
@RequiredArgsConstructor
public class PostRedisServiceImpl implements PostRedisService{

    private final PostRedisStatusManager postRedisStatusManager;
    private final PostApiResponseFactory postApiResponseFactory;

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> getPostStatus(String tempId, String eventType){

        PostStatus postStatus = postRedisStatusManager.getStatus(PostEventType.valueOf(eventType.toUpperCase()), tempId);

        return postApiResponseFactory.buildResponse(
                tempId,
                postStatus,
                PostEventType.valueOf(eventType.toUpperCase())
        );
    };
}
