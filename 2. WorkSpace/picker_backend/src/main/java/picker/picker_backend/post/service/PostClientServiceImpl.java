package picker.picker_backend.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.component.helper.PostRedisHashMapHelper;
import picker.picker_backend.post.component.helper.PostTopicKeyMapperHelper;
import picker.picker_backend.post.factory.PostApiResponseFactory;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.*;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.component.manger.PostRedisStatusManager;
import picker.picker_backend.post.kafka.producer.PostEventProducer;
import picker.picker_backend.post.postenum.Status;
import picker.picker_backend.post.postenum.TopicKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostClientServiceImpl implements PostClientService {

    private final PostEventProducer postEventProducer;
    private final PostRedisHashMapHelper postRedisHashMapHelper;
    private final PostRedisStatusManager postRedisStatusManager;
    private final PostTopicKeyMapperHelper postTopicKeyMapperHelper;

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> insertPost(PostInsertRequestDTO postInsertRequestDTO){
        try{

            postRedisStatusManager.setStatusMap(
                    EventType.INSERT,
                    postInsertRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(postInsertRequestDTO, EventType.INSERT),
                    TopicKey.POST
            );

            postEventProducer.sendPostEvent(postInsertRequestDTO, EventType.INSERT);

            return PostApiResponseFactory.buildResponse(
                    postInsertRequestDTO.getTempId(),
                    Status.PROCESSING,
                    EventType.INSERT,
                    TopicKey.POST
            );


        } catch (Exception e) {

            postRedisStatusManager.setStatusWithTimestamp(EventType.INSERT,
                    postInsertRequestDTO.getTempId(),
                    Status.FAILED,
                    postTopicKeyMapperHelper.getTopicName(TopicKey.POST)
            );

            log.error("Error",e);

            throw e;

        }
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> updatePost(PostUpdateRequestDTO postUpdateRequestDTO){
        try{

            postRedisStatusManager.setStatusMap(
                    EventType.UPDATE,
                    postUpdateRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(postUpdateRequestDTO, EventType.UPDATE),
                    TopicKey.POST

            );

            postEventProducer.sendPostEvent(postUpdateRequestDTO, EventType.UPDATE);

            return PostApiResponseFactory.buildResponse(
                    postUpdateRequestDTO.getTempId(),
                    Status.PROCESSING,
                    EventType.UPDATE,
                    TopicKey.POST
            );

        } catch (Exception e) {

            postRedisStatusManager.setStatusWithTimestamp(
                    EventType.UPDATE,
                    postUpdateRequestDTO.getTempId(),
                    Status.FAILED,
                    postTopicKeyMapperHelper.getTopicName(TopicKey.POST)
            );

            log.error("Error",e);

            throw e;

        }
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> deletePost(PostDeleteRequestDTO postDeleteRequestDTO){
        try{
            postRedisStatusManager.setStatusMap(
                    EventType.DELETE,
                    postDeleteRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(postDeleteRequestDTO, EventType.DELETE),
                    TopicKey.POST
            );

            postEventProducer.sendPostEvent(postDeleteRequestDTO, EventType.DELETE);

            return PostApiResponseFactory.buildResponse(
                    postDeleteRequestDTO.getTempId(),
                    Status.PROCESSING,
                    EventType.DELETE,
                    TopicKey.POST
            );

        } catch (Exception e) {

            postRedisStatusManager.setStatusWithTimestamp(
                    EventType.DELETE,
                    postDeleteRequestDTO.getTempId(),
                    Status.FAILED,
                    postTopicKeyMapperHelper.getTopicName(TopicKey.POST)
            );

            log.error("Error",e);

            throw e;

        }
    }

}
