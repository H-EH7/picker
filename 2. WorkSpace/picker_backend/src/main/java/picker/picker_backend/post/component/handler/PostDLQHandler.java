package picker.picker_backend.post.component.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.PostDBManager;
import picker.picker_backend.post.component.manger.PostRedisStatusManager;
import picker.picker_backend.post.component.manger.PostRedisViewCountManager;
import picker.picker_backend.post.model.dto.PostDeleteRequestDTO;
import picker.picker_backend.post.model.dto.PostInsertRequestDTO;
import picker.picker_backend.post.model.dto.PostUpdateRequestDTO;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.Status;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostDLQHandler {
    private final PostDBManager postDBManager;
    private final ObjectMapper objectMapper;
    private final PostRedisStatusManager postRedisStatusManager;
    private final PostRedisViewCountManager postRedisViewCountManager;

    public void postDLQEvent(EventType eventType, String message, String topic){

        BiConsumer<String, String> postDLQHandler = eventTypeHandleMap.get(eventType);

        if(postDLQHandler != null){
            postDLQHandler.accept(message, topic);
        }else{
            log.error("Unknown Event Type : {} ",eventType);
        }
    }

    private final Map<EventType, BiConsumer<String, String>> eventTypeHandleMap = Map.of(
            EventType.INSERT, this::handleDLQInsertEvent,
            EventType.UPDATE, this::handleDLQUpdateEvent,
            EventType.DELETE, this::handleDLQDeleteEvent
    );

    private <T> void processEvent(String message,
                                  Class<T> dtoClass,
                                  Function<T, CompletableFuture<?>> dbFunction,
                                  EventType eventType,
                                  String topic){
        try{
            T eventDTO = objectMapper.readValue(message, dtoClass);
            CompletableFuture<?> future = dbFunction.apply(eventDTO);
            future.whenComplete((result, exception) -> {
                if(exception != null){
                    dlqHandleFailure(eventType, exception, eventDTO, topic);
                }else{
                    dlqHandleSuccess(eventType, result, eventDTO, topic);
                }
            });
        }catch (Exception e) {
            log.error("{} eventProcessing Error ", eventType, e);
        }
    }
    private void dlqHandleFailure(EventType eventType, Throwable exception, Object eventDTO, String topic){
        log.error("{} DLQ Fail", eventType.name(), exception);
        postRedisStatusManager.setStatusWithTimestamp(eventType, getTempId(eventDTO), Status.DLQ_FAILED, topic);
    }

    private void dlqHandleSuccess(EventType eventType, Object result, Object eventDTO, String topic){
        postRedisStatusManager.setStatusWithTimestamp(eventType, getTempId(eventDTO), Status.SUCCESS, topic);
        if(eventType == EventType.INSERT){
            postRedisViewCountManager.setViewCount((long)result);
        } else if (eventType == EventType.DELETE) {
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

    private void handleDLQInsertEvent(String message, String topic){
        processEvent(message,
                PostInsertRequestDTO.class,
                postDBManager::postDBInsert,
                EventType.INSERT,
                topic);
    }

    private void handleDLQUpdateEvent(String message,String topic){
        processEvent(message,
                PostUpdateRequestDTO.class,
                postDBManager::postDBUpdate,
                EventType.UPDATE,
                topic);
    }

    private void handleDLQDeleteEvent(String message, String topic){
        processEvent(message,
                PostDeleteRequestDTO.class,
                postDBManager::postDBDelete,
                EventType.DELETE,
                topic);
    }

}
