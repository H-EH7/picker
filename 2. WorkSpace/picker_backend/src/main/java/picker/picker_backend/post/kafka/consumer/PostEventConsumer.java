package picker.picker_backend.post.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.handler.PostKafkaConsumerHandler;
import picker.picker_backend.post.postenum.PostEventType;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventConsumer {

    private final PostKafkaConsumerHandler postKafkaConsumerHandler;

    @KafkaListener(topics = "post-events", groupId = "post-group")
    public void receivePostEvent(ConsumerRecord<String, String> record){
        String eventType = record.key();
        String message = record.value();
        try {

            postKafkaConsumerHandler.postConsumerEvent(
                    PostEventType.valueOf(eventType.toUpperCase()),
                    message
            );

        }catch (Exception e){
            log.error("kafka consumer fail", e);
        }
    }

}
