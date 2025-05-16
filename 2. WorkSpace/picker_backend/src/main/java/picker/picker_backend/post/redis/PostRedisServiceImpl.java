package picker.picker_backend.post.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.component.helper.PostTopicKeyMapperHelper;
import picker.picker_backend.post.component.manger.PostRedisStatusManager;
import picker.picker_backend.post.factory.PostApiResponseFactory;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.PostResponseDTO;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.Status;
import picker.picker_backend.post.postenum.TopicKey;

@Service
@RequiredArgsConstructor
public class PostRedisServiceImpl implements PostRedisService{

    private final PostRedisStatusManager postRedisStatusManager;
    private final PostTopicKeyMapperHelper postTopicKeyMapperHelper;

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> getPostStatus(String tempId, String eventType){

        Status status = postRedisStatusManager.getStatus(EventType.valueOf(eventType.toUpperCase()), tempId, postTopicKeyMapperHelper.getTopicName(TopicKey.POST));

        return PostApiResponseFactory.buildResponse(
                tempId,
                status,
                EventType.valueOf(eventType.toUpperCase()),
                TopicKey.POST
        );
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> getReplyStatus(String tempId, String eventType) {

        Status status = postRedisStatusManager.getStatus(EventType.valueOf(eventType.toUpperCase()), tempId, postTopicKeyMapperHelper.getTopicName(TopicKey.REPLY));

        return PostApiResponseFactory.buildResponse(
                tempId,
                status,
                EventType.valueOf(eventType.toUpperCase()),
                TopicKey.REPLY
        );
    }
}
