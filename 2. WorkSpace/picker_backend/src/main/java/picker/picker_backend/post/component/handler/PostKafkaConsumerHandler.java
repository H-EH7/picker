package picker.picker_backend.post.component.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.helper.DBHandlerHelper;
import picker.picker_backend.post.component.helper.PostTopicKeyMapperHelper;
import picker.picker_backend.post.component.manger.PostDLQManager;
import picker.picker_backend.post.component.manger.PostRedisStatusManager;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;
import picker.picker_backend.post.model.common.TempIdSupport;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.Status;
import picker.picker_backend.post.postenum.TopicKey;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostKafkaConsumerHandler {

    private final ObjectMapper objectMapper;
    private final PostDLQManager postDLQManager;
    private final PostRedisStatusManager postRedisStatusManager;
    private final DBHandlerHelper dbHandlerHelper;
    private final PostTopicKeyMapperHelper postTopicKeyMapperHelper;
    private final RedistEventHandler redistEventHandler;

    public void postConsumerEvent(String topic, EventType eventType, String message){

        DBHandler dbHandler = dbHandlerHelper.getDBhandler(postTopicKeyMapperHelper.getTopicKey(topic).name());

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
                    handleFailure(eventType, message, ex, dto, topic);
                }else {
                    handleSuccess(eventType, result, dto, topic);
                }
            });

        }catch (Exception e){
            log.error("Failed consumer",e);
        }

    }

    private void handleFailure(EventType eventType, String message, Throwable exception, Object eventDTO, String topic){
        log.error("{} Send To DLQ", eventType.name(), exception);
        postDLQManager.dlqConsumerEvent(topic,message,eventType);
        String tempId = (eventDTO instanceof TempIdSupport support) ? support.getTempId() : null;
        postRedisStatusManager.setStatusWithTimestamp(eventType, tempId, Status.DLQ_PROCESSING, topic);
    }

    private void handleSuccess(EventType eventType, Object result, Object eventDTO, String topic){
        String tempId = (eventDTO instanceof TempIdSupport support) ? support.getTempId() : null;
        postRedisStatusManager.setStatusWithTimestamp(eventType, tempId, Status.SUCCESS,topic);

        TopicKey topicKey = postTopicKeyMapperHelper.getTopicKey(topic);
        redistEventHandler.handler(topicKey, eventType, (Long)result);
    }


}
