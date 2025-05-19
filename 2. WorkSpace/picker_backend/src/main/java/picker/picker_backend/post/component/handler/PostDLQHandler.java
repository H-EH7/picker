package picker.picker_backend.post.component.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.helper.DBHandlerHelper;
import picker.picker_backend.post.component.helper.PostTopicKeyMapperHelper;
import picker.picker_backend.post.component.manger.PostRedisStatusManager;
import picker.picker_backend.post.model.common.TempIdSupport;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.Status;
import picker.picker_backend.post.postenum.TopicKey;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostDLQHandler {
    private final ObjectMapper objectMapper;
    private final PostRedisStatusManager postRedisStatusManager;
    private final DBHandlerHelper dbHandlerHelper;
    private final PostTopicKeyMapperHelper postTopicKeyMapperHelper;
    private final RedisEventHandler redisEventHandler;

    public void postDLQEvent(EventType eventType, String message, String topic){

        DBHandler dbHandler = dbHandlerHelper.getDBhandler(postTopicKeyMapperHelper.getDLQTopicKey(topic).name());

        if(dbHandler == null){
            log.error("Unknow topic : {}", topic);
            return;
        }

        try{
            Class<?> dtoClass = dbHandler.getDtoClass(eventType);
            Object dto =objectMapper.readValue(message, dtoClass);

            CompletableFuture<?> future = switch (eventType){
                case INSERT -> dbHandler.getDbManger().insert(dto);
                case UPDATE -> dbHandler.getDbManger().update(dto);
                case DELETE -> dbHandler.getDbManger().delete(dto);
            };

            future.whenComplete((result ,ex) ->{
                if(ex != null){
                    dlqHandleFailure(eventType, ex, dto, topic);
                }else {
                    dlqHandleSuccess(eventType, result, dto, topic);
                }
            });
        }catch (Exception e){
            log.error("Failed consumer",e);

        }
    }

    private void dlqHandleFailure(EventType eventType, Throwable exception, Object eventDTO, String topic){
        log.error("{} DLQ Fail", eventType.name(), exception);
        String tempId = (eventDTO instanceof TempIdSupport support) ? support.getTempId() : null;
        if(tempId != null) {
            postRedisStatusManager.setStatusWithTimestamp(eventType, tempId, Status.DLQ_FAILED, topic);
        }
    }

    private void dlqHandleSuccess(EventType eventType, Object result, Object eventDTO, String topic){
        String tempId = (eventDTO instanceof TempIdSupport support) ? support.getTempId() : null;
        TopicKey topicKey = postTopicKeyMapperHelper.getDLQTopicKey(topic);
        if(tempId != null){
            postRedisStatusManager.setStatusWithTimestamp(eventType, tempId, Status.SUCCESS, topic);
        }
        redisEventHandler.handler(topicKey, eventType, (Long)result);
    }

}
