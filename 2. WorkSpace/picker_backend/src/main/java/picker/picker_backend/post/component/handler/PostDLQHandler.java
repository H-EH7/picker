package picker.picker_backend.post.component.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.PostDBManager;
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
public class PostDLQHandler {
    private final PostDBManager postDBManager;
    private final ObjectMapper objectMapper;
    private final PostRedisStatusManager postRedisStatusManager;

    public void postDLQEvent(PostEventType eventType, String message){

        Consumer<String> postDLQHandler = eventTypeHandleMap.get(eventType);

        if(postDLQHandler != null){
            postDLQHandler.accept(message);
        }else{
            log.error("Unknown Event Type : {} ",eventType);
        }
    }

    private final Map<PostEventType, Consumer<String>> eventTypeHandleMap = Map.of(
            PostEventType.INSERT, this::handleDLQInsertEvent,
            PostEventType.UPDATE, this::handleDLQUpdateEvent,
            PostEventType.DELETE, this::handleDLQDeleteEvent
    );

    private void handleDLQInsertEvent(String message){
        try{

            PostInsertRequestDTO postInsertRequestDTO = objectMapper.readValue(message, PostInsertRequestDTO.class);
            CompletableFuture<Void> future = postDBManager.postDBInsert(postInsertRequestDTO);
            future.whenComplete((result, exception) -> {
                if(exception != null){
                    log.error("DB fail DLQ", exception);

                    postRedisStatusManager.setStatusWithTimestamp(
                            PostEventType.INSERT,
                            postInsertRequestDTO.getTempId(),
                            PostStatus.DLQ_FAILED
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
            log.error("post DLQ insert error", e);
        }
    }

    private void handleDLQUpdateEvent(String message){
        try{
            PostUpdateRequestDTO postUpdateRequestDTO = objectMapper.readValue(message, PostUpdateRequestDTO.class);
            CompletableFuture<Void> future = postDBManager.postDBUpdate(postUpdateRequestDTO);
            future.whenComplete((result, exception) -> {
                if(exception != null){
                    log.error("DB fail DLQ", exception);

                    postRedisStatusManager.setStatusWithTimestamp(
                            PostEventType.UPDATE,
                            postUpdateRequestDTO.getTempId(),
                            PostStatus.DLQ_FAILED
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
            log.error("post DLQ update error", e);
        }
    }

    private void handleDLQDeleteEvent(String message){
        try{
            PostDeleteRequestDTO postDeleteRequestDTO = objectMapper.readValue(message, PostDeleteRequestDTO.class);
            CompletableFuture<Void> future = postDBManager.postDBDelete(postDeleteRequestDTO);
            future.whenComplete((result, exception) -> {
                if(exception != null){
                    log.error("DB fail DLQ", exception);

                    postRedisStatusManager.setStatusWithTimestamp(
                            PostEventType.DELETE,
                            postDeleteRequestDTO.getTempId(),
                            PostStatus.DLQ_FAILED
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
            log.error("post DLQ delete error", e);
        }
    }

}
