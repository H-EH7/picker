package picker.picker_backend.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.component.helper.PostRedisHashMapHelper;
import picker.picker_backend.post.factory.PostApiResponseFactory;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.model.dto.*;
import picker.picker_backend.post.postenum.PostEventType;
import picker.picker_backend.post.component.manger.PostRedisStatusManager;
import picker.picker_backend.post.kafka.producer.PostEventProducer;
import picker.picker_backend.post.postenum.PostStatus;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostClientServiceImpl implements PostClientService {

    private final PostEventProducer postEventProducer;
    private final PostRedisHashMapHelper postRedisHashMapHelper;
    private final PostRedisStatusManager postRedisStatusManager;
    private final PostApiResponseFactory postApiResponseFactory;

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> insertPost(PostInsertRequestDTO postInsertRequestDTO){
        try{

            postRedisStatusManager.setStatusMap(
                    PostEventType.INSERT,
                    postInsertRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(postInsertRequestDTO, PostEventType.INSERT)
            );

            postEventProducer.sendPostEvent(postInsertRequestDTO, PostEventType.INSERT);

            return postApiResponseFactory.buildResponse(
                    postInsertRequestDTO.getTempId(),
                    PostStatus.PROCESSING,
                    PostEventType.INSERT
            );


        } catch (Exception e) {

            postRedisStatusManager.setStatusWithTimestamp(PostEventType.INSERT,
                    postInsertRequestDTO.getTempId(),
                    PostStatus.FAILED
            );

            log.error("Error",e);

            throw e;

        }
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> updatePost(PostUpdateRequestDTO postUpdateRequestDTO){
        try{

            postRedisStatusManager.setStatusMap(
                    PostEventType.UPDATE,
                    postUpdateRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(postUpdateRequestDTO, PostEventType.UPDATE)
            );

            postEventProducer.sendPostEvent(postUpdateRequestDTO, PostEventType.UPDATE);

            return postApiResponseFactory.buildResponse(
                    postUpdateRequestDTO.getTempId(),
                    PostStatus.PROCESSING,
                    PostEventType.UPDATE
            );

        } catch (Exception e) {

            postRedisStatusManager.setStatusWithTimestamp(
                    PostEventType.UPDATE,
                    postUpdateRequestDTO.getTempId(),
                    PostStatus.FAILED
            );

            log.error("Error",e);

            throw e;

        }
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> deletePost(PostDeleteRequestDTO postDeleteRequestDTO){
        try{
            postRedisStatusManager.setStatusMap(
                    PostEventType.DELETE,
                    postDeleteRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(postDeleteRequestDTO, PostEventType.DELETE)
            );

            postEventProducer.sendPostEvent(postDeleteRequestDTO, PostEventType.DELETE);

            return postApiResponseFactory.buildResponse(
                    postDeleteRequestDTO.getTempId(),
                    PostStatus.PROCESSING,
                    PostEventType.DELETE
            );

        } catch (Exception e) {

            postRedisStatusManager.setStatusWithTimestamp(
                    PostEventType.DELETE,
                    postDeleteRequestDTO.getTempId(),
                    PostStatus.FAILED
            );

            log.error("Error",e);

            throw e;

        }
    }

}
