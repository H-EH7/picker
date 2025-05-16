package picker.picker_backend.post.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.handler.PostDLQHandler;
import picker.picker_backend.post.postenum.EventType;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostDLQConsumer {

    private final PostDLQHandler postDLQHandler;

    @KafkaListener(topics = "post-dlq", groupId = "post-group")
    public void receiveDLQPostEvent(ConsumerRecord<String, String> record){
        String eventType = record.key();
        String message = record.value();
        String topic = record.topic();

        try {

            postDLQHandler.postDLQEvent(
                    EventType.valueOf(eventType.toUpperCase()),
                    message,
                    topic

            );

        }catch (Exception e){
            log.error("kafka DLQ consumer fail", e);
        }
    }

    @KafkaListener(topics = "reply-dlq", groupId = "post-group")
    public void receiveDLQReplyEvent(ConsumerRecord<String, String> record){
        String eventType = record.key();
        String message = record.value();
        String topic = record.topic();

        try {

            postDLQHandler.postDLQEvent(
                    EventType.valueOf(eventType.toUpperCase()),
                    message,
                    topic

            );

        }catch (Exception e){
            log.error("kafka DLQ consumer fail", e);
        }
    }
}
