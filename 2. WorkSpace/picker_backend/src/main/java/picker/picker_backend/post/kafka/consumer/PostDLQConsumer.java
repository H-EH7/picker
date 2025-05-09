package picker.picker_backend.post.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.model.dto.PostInsertDTO;
import picker.picker_backend.post.model.dto.PostUpdateDTO;
import picker.picker_backend.post.service.PostDBService;
import picker.picker_backend.post.service.PostDLQService;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostDLQConsumer {

    private final PostDBService postDBService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PostDLQService postDLQService;

    @KafkaListener(topics = "post-dlq", groupId = "post-group")
    public void receiveDLQPostEvent(ConsumerRecord<String, String> record){
        String eventType = record.key();
        String message = record.value();

        try {
            switch (eventType){
                case "insert" :
                    PostInsertDTO postInsertDTO = objectMapper.readValue(message, PostInsertDTO.class);
                    postDBService.postDBInsert(postInsertDTO);
                    break;
                case "update" :
                    PostUpdateDTO postUpdateDTO = objectMapper.readValue(message, PostUpdateDTO.class);
                    postDBService.postDBUpdate(postUpdateDTO);
                    break;
                default:
                    log.error("Unknown Type DLQ {}", eventType);
            }

        }catch (Exception e){
            log.error("kafka DLQ consumer fail", e);
        }
    }
}
