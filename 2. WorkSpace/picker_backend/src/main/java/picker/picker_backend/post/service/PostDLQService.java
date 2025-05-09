package picker.picker_backend.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostDLQService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Async("postDLQExecutor")
    public void sendToDLQ(String eventType, String message){

        try{
            kafkaTemplate.send("post-dlq", eventType, message);
        }catch (Exception e){
            log.error("Faile to send to DLQ", e);
        }
    }

}
