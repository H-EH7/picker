package picker.picker_backend.post.component.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.PostDBManager;
import picker.picker_backend.post.component.manger.PostDLQManager;
import picker.picker_backend.post.component.manger.PostRedisStatusManager;
import picker.picker_backend.post.model.dto.PostDeleteRequestDTO;
import picker.picker_backend.post.model.dto.PostInsertRequestDTO;
import picker.picker_backend.post.model.dto.PostUpdateRequestDTO;
import picker.picker_backend.post.postenum.PostEventType;
import picker.picker_backend.post.postenum.PostStatus;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostKafkaConsumerHandler {

    private final PostDBManager postDBManager;
    private final ObjectMapper objectMapper;
    private final PostDLQManager postDLQManager;
    private final PostRedisStatusManager postRedisStatusManager;

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

    private void handleInsertEvent(String message){
        try{

            PostInsertRequestDTO postInsertRequestDTO = objectMapper.readValue(message, PostInsertRequestDTO.class);
            CompletableFuture<Void> future = postDBManager.postDBInsert(postInsertRequestDTO);
            future.whenComplete((result, exception) -> {
                if(exception != null){
                    log.error("DB fail consumer", exception);

                    postDLQManager.postSendToDLQ(PostEventType.INSERT, message);

                    postRedisStatusManager.setStatusWithTimestamp(
                            PostEventType.INSERT,
                            postInsertRequestDTO.getTempId(),
                            PostStatus.DLQ_PROCESSING
                    );

                }else{

                    postRedisStatusManager.setStatusWithTimestamp(
                            PostEventType.INSERT,
                            postInsertRequestDTO.getTempId(),
                            PostStatus.SUCCESS
                    );
                }
            });
        }catch (Exception e){
            log.error("post insert error", e);
        }
    }

    private void handleUpdateEvent(String message){
        try{
            PostUpdateRequestDTO postUpdateRequestDTO = objectMapper.readValue(message, PostUpdateRequestDTO.class);
            CompletableFuture<Void> future = postDBManager.postDBUpdate(postUpdateRequestDTO);
            future.whenComplete((result, exception) -> {
                if(exception != null){
                    log.error("DB fail consumer", exception);

                    postDLQManager.postSendToDLQ(PostEventType.UPDATE, message);

                    postRedisStatusManager.setStatusWithTimestamp(
                            PostEventType.UPDATE,
                            postUpdateRequestDTO.getTempId()
                            ,PostStatus.DLQ_PROCESSING
                    );

                }else{
                    postRedisStatusManager.setStatusWithTimestamp(
                            PostEventType.UPDATE,
                            postUpdateRequestDTO.getTempId(),
                            PostStatus.SUCCESS
                    );
                }
            });
        }catch (Exception e){
            log.error("post update error", e);
        }
    }

    private void handleDeleteEvent(String message){
        try{
            PostDeleteRequestDTO postDeleteRequestDTO = objectMapper.readValue(message, PostDeleteRequestDTO.class);
            CompletableFuture<Void> future = postDBManager.postDBDelete(postDeleteRequestDTO);
            future.whenComplete((result, exception) -> {
                if(exception != null){
                    log.error("DB fail consumer", exception);

                    postDLQManager.postSendToDLQ(PostEventType.DELETE, message);

                    postRedisStatusManager.setStatusWithTimestamp(
                            PostEventType.DELETE,
                            postDeleteRequestDTO.getTempId()
                            ,PostStatus.DLQ_PROCESSING
                    );

                }else{
                    postRedisStatusManager.setStatusWithTimestamp(
                            PostEventType.DELETE,
                            postDeleteRequestDTO.getTempId(),
                            PostStatus.SUCCESS
                    );
                }
            });
        }catch (Exception e){
            log.error("post delete error", e);
        }
    }
}
