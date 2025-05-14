package picker.picker_backend.post.component.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.PostDBManager;
import picker.picker_backend.post.component.manger.PostDLQManager;
import picker.picker_backend.post.component.manger.PostRedisStatusManager;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;
import picker.picker_backend.post.model.dto.PostDeleteRequestDTO;
import picker.picker_backend.post.model.dto.PostInsertRequestDTO;
import picker.picker_backend.post.model.dto.PostUpdateRequestDTO;
import picker.picker_backend.post.postenum.PostEventType;
import picker.picker_backend.post.postenum.PostStatus;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostKafkaConsumerHandler {

    private final PostDBManager postDBManager;
    private final ObjectMapper objectMapper;
    private final PostDLQManager postDLQManager;
    private final PostRedisStatusManager postRedisStatusManager;
    private final PostRedisViewCountManager postRedisViewCountManager;

    public void postConsumerEvent(PostEventType eventType, String message){

        Consumer<String> postConsumerHandler = eventTypeHandleMap.get(eventType);

        if(postConsumerHandler != null){
            postConsumerHandler.accept(message);
        }else{
            log.error("Unknown Event Type : {} ",eventType);
        }
    }

    private final Map<PostEventType, Consumer<String>> eventTypeHandleMap = Map.of(
            PostEventType.INSERT, this::handleInsertEvent,
            PostEventType.UPDATE, this::handleUpdateEvent,
            PostEventType.DELETE, this::handleDeleteEvent
    );

    private <T> void processEvent(String message,
                                  Class<T> dtoClass,
                                  Function<T, CompletableFuture<?>> dbFunction,
                                  PostEventType eventType){
        try{
            T eventDTO = objectMapper.readValue(message, dtoClass);
            CompletableFuture<?> future = dbFunction.apply(eventDTO);
            future.whenComplete((result, exception) -> {
               if(exception != null){
                   handleFailure(eventType,message,exception,eventDTO);
               }else{
                   handleSuccess(eventType, result, eventDTO);
               }
            });

        }catch (Exception e){
            log.error("{} eventProcessing Error ", eventType, e);
        }

    }

    private void handleInsertEvent(String message){
        processEvent(message,
                PostInsertRequestDTO.class,
                postDBManager::postDBInsert,
                PostEventType.INSERT);
    }

    private void handleUpdateEvent(String message){
        processEvent(message,
                PostUpdateRequestDTO.class,
                postDBManager::postDBUpdate,
                PostEventType.UPDATE);
    }

    private void handleDeleteEvent(String message){
        processEvent(message,
                PostDeleteRequestDTO.class,
                postDBManager::postDBDelete,
                PostEventType.DELETE);
    }

    private void handleFailure(PostEventType eventType, String message, Throwable exception, Object eventDTO){
        log.error(eventType.name() + " Send To DLQ");
        postDLQManager.postSendToDLQ(eventType,message);
        postRedisStatusManager.setStatusWithTimestamp(eventType,  getTempId(eventDTO), PostStatus.DLQ_PROCESSING);
    }

    private void handleSuccess(PostEventType eventType, Object result, Object eventDTO){
        postRedisStatusManager.setStatusWithTimestamp(eventType, getTempId(eventDTO), PostStatus.SUCCESS);
        if(eventType == PostEventType.INSERT){
            postRedisViewCountManager.setViewCount((long)result);
        } else if (eventType == PostEventType.DELETE) {
            postRedisViewCountManager.deleteViewCount((long)result);
        }
    }

    private String getTempId(Object eventDTO){
        if(eventDTO instanceof PostInsertRequestDTO){
            return((PostInsertRequestDTO) eventDTO).getTempId();
        }else if(eventDTO instanceof PostUpdateRequestDTO){
            return ((PostUpdateRequestDTO)eventDTO).getTempId();
        } else if (eventDTO instanceof PostDeleteRequestDTO) {
            return (((PostDeleteRequestDTO) eventDTO).getTempId());
        }
        return null;
    }
}
