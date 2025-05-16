package picker.picker_backend.post.reply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.component.helper.PostRedisHashMapHelper;
import picker.picker_backend.post.component.helper.PostTopicKeyMapperHelper;
import picker.picker_backend.post.component.manger.PostRedisStatusManager;
import picker.picker_backend.post.factory.PostApiResponseFactory;
import picker.picker_backend.post.factory.PostApiResponseWrapper;
import picker.picker_backend.post.kafka.producer.PostEventProducer;
import picker.picker_backend.post.model.dto.PostResponseDTO;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.Status;
import picker.picker_backend.post.postenum.TopicKey;
import picker.picker_backend.post.reply.model.dto.ReplyDeleteRequestDTO;
import picker.picker_backend.post.reply.model.dto.ReplyInsertRequestDTO;
import picker.picker_backend.post.reply.model.dto.ReplyUpdateRequestDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyClientServiceImpl implements ReplyClientService{

    private final PostRedisStatusManager postRedisStatusManager;
    private final PostRedisHashMapHelper postRedisHashMapHelper;
    private final PostEventProducer postEventProducer;
    private final PostTopicKeyMapperHelper postTopicKeyMapperHelper;

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> insertReply(ReplyInsertRequestDTO replyInsertRequestDTO){
        try{

           postRedisStatusManager.setStatusMap(
                    EventType.INSERT,
                    replyInsertRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(EventType.INSERT, replyInsertRequestDTO),
                    TopicKey.REPLY
            );

            postEventProducer.sendReplyEvent(replyInsertRequestDTO, EventType.INSERT);

            return PostApiResponseFactory.buildResponse(
                    replyInsertRequestDTO.getTempId(),
                    Status.PROCESSING,
                    EventType.INSERT,
                    TopicKey.REPLY
            );

        } catch (Exception e) {

            postRedisStatusManager.setStatusWithTimestamp(EventType.INSERT,
                    replyInsertRequestDTO.getTempId(),
                    Status.FAILED,
                    postTopicKeyMapperHelper.getTopicName(TopicKey.REPLY)
            );

            log.error("Error",e);

            throw e;

        }
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> updateReply(ReplyUpdateRequestDTO replyUpdateRequestDTO) {
    try{
        postRedisStatusManager.setStatusMap(
                EventType.UPDATE,
                replyUpdateRequestDTO.getTempId(),
                postRedisHashMapHelper.createRedisHashMap(EventType.UPDATE, replyUpdateRequestDTO),
                TopicKey.REPLY
        );

        postEventProducer.sendReplyEvent(replyUpdateRequestDTO, EventType.UPDATE);

        return PostApiResponseFactory.buildResponse(
                replyUpdateRequestDTO.getTempId(),
                Status.PROCESSING,
                EventType.UPDATE,
                TopicKey.REPLY
        );

        } catch (Exception e){

            postRedisStatusManager.setStatusWithTimestamp(EventType.UPDATE,
                    replyUpdateRequestDTO.getTempId(),
                    Status.FAILED,
                    postTopicKeyMapperHelper.getTopicName(TopicKey.REPLY)
            );

            log.error("Error", e);

            throw e;
        }
    }

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> deleteReply(ReplyDeleteRequestDTO replyDeleteRequestDTO) {
        try{
            postRedisStatusManager.setStatusMap(
                    EventType.DELETE,
                    replyDeleteRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(EventType.DELETE, replyDeleteRequestDTO),
                    TopicKey.REPLY
            );

            postEventProducer.sendReplyEvent(replyDeleteRequestDTO, EventType.DELETE);

            return PostApiResponseFactory.buildResponse(
                    replyDeleteRequestDTO.getTempId(),
                    Status.PROCESSING,
                    EventType.DELETE,
                    TopicKey.REPLY
            );

        } catch (Exception e){

            postRedisStatusManager.setStatusWithTimestamp(EventType.DELETE,
                    replyDeleteRequestDTO.getTempId(),
                    Status.FAILED,
                    postTopicKeyMapperHelper.getTopicName(TopicKey.REPLY)
            );

            log.error("Error", e);

            throw e;
        }
    }
}
