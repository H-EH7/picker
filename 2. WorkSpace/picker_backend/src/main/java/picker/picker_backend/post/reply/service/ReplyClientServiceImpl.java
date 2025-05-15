package picker.picker_backend.post.reply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import picker.picker_backend.post.reply.model.dto.ReplyInsertRequestDTO;
import picker.picker_backend.post.reply.model.dto.ReplyResponseDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyClientServiceImpl implements ReplyClientService{

    private PostRedisStatusManager postRedisStatusManager;
    private PostRedisHashMapHelper postRedisHashMapHelper;
    private PostEventProducer postEventProducer;
    private PostTopicKeyMapperHelper postTopicKeyMapperHelper;

    @Override
    public ResponseEntity<PostApiResponseWrapper<PostResponseDTO>> insertReply(ReplyInsertRequestDTO replyInsertRequestDTO){
        try{

           postRedisStatusManager.setStatusMap(
                    EventType.INSERT,
                    replyInsertRequestDTO.getTempId(),
                    postRedisHashMapHelper.createRedisHashMap(replyInsertRequestDTO, EventType.INSERT),
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

}
