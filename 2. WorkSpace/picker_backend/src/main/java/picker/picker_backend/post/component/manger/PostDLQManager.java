package picker.picker_backend.post.component.manger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.postenum.PostEventType;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostDLQManager {

    private final KafkaTemplate<String, String > kafkaTemplate;

    @Async("postDLQExecutor")
    public void postSendToDLQ(PostEventType eventType, String message){

        try{

            kafkaTemplate.send("post-dlq",eventType.name(), message);

        }catch (Exception e){

            log.error("Failed to send to DLQ", e);

        }
    }

}
