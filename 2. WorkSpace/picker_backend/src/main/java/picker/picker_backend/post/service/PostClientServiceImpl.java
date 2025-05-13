package picker.picker_backend.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public PostApiResponseWrapper<PostResponseDTO> insertPost(PostInsertRequestDTO postInsertRequestDTO){
        try{

            postRedisStatusManager.setStatusMap(
                    PostEventType.INSERT,
                    postInsertRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(postInsertRequestDTO, PostEventType.INSERT)
            );

            postEventProducer.sendPostEvent(postInsertRequestDTO, PostEventType.INSERT);

            PostStatus postStatus = postRedisStatusManager.getStatus(PostEventType.INSERT, postInsertRequestDTO.getTempId());

            if(postStatus == PostStatus.SUCCESS){
                postRedisStatusManager.setExpireTime(PostEventType.INSERT, postInsertRequestDTO.getTempId());
            }

            return postApiResponseFactory.buildResponse(postInsertRequestDTO.getTempId(),
                    postStatus,
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
    public PostApiResponseWrapper<PostResponseDTO> updatePost(PostUpdateRequestDTO postUpdateRequestDTO){
        try{

            postRedisStatusManager.setStatusMap(
                    PostEventType.UPDATE,
                    postUpdateRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(postUpdateRequestDTO, PostEventType.UPDATE)
            );

            postEventProducer.sendPostEvent(postUpdateRequestDTO, PostEventType.UPDATE);

            PostStatus postStatus = postRedisStatusManager.getStatus(PostEventType.UPDATE, postUpdateRequestDTO.getTempId());

            if(postStatus == PostStatus.SUCCESS){
                postRedisStatusManager.setExpireTime(PostEventType.UPDATE, postUpdateRequestDTO.getTempId());
            }

            return postApiResponseFactory.buildResponse(
                    postUpdateRequestDTO.getTempId(),
                    postStatus,
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
    public PostApiResponseWrapper<PostResponseDTO> deletePost(PostDeleteRequestDTO postDeleteRequestDTO){
        try{
            postRedisStatusManager.setStatusMap(
                    PostEventType.DELETE,
                    postDeleteRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(postDeleteRequestDTO, PostEventType.DELETE)
            );

            postEventProducer.sendPostEvent(postDeleteRequestDTO, PostEventType.DELETE);

            PostStatus postStatus = postRedisStatusManager.getStatus(PostEventType.DELETE, postDeleteRequestDTO.getTempId());

            if(postStatus == PostStatus.SUCCESS){
                postRedisStatusManager.setExpireTime(PostEventType.DELETE, postDeleteRequestDTO.getTempId());
            }

            return postApiResponseFactory.buildResponse(
                    postDeleteRequestDTO.getTempId(),
                    postStatus,
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
