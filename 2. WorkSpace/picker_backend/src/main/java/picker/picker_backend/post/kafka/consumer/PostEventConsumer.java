package picker.picker_backend.post.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.handler.PostKafkaConsumerHandler;
import picker.picker_backend.post.postenum.EventType;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventConsumer {

    private final PostKafkaConsumerHandler postKafkaConsumerHandler;

    @KafkaListener(topics = "${post.kafka.topic.post}", groupId = "${post.kafka.groupId}")
    public void receivePostEvent(ConsumerRecord<String, String> record){
        String eventType = record.key();
        String message = record.value();
        String topic = record.topic();
        try {

            postKafkaConsumerHandler.postConsumerEvent(
                    topic,
                    EventType.valueOf(eventType.toUpperCase()),
                    message
            );

        }catch (Exception e){
            log.error("kafka consumer fail", e);
        }
    }

    @KafkaListener(topics = "${post.kafka.topic.reply}", groupId = "${post.kafka.groupId}")
    public void receiveReplyEvent(ConsumerRecord<String, String> record){
        String eventType = record.key();
        String message = record.value();
        String topic = record.topic();
        try {

            postKafkaConsumerHandler.postConsumerEvent(
                    topic,
                    EventType.valueOf(eventType.toUpperCase()),
                    message
            );
        }catch (Exception e){
            log.error("kafka consumer fail", e);
        }
    }

    @KafkaListener(topics = "${post.kafka.topic.likes}", groupId = "${post.kafka.groupId}")
    public void receiveLikesEvent(ConsumerRecord<String, String> record){
        String evenType = record.key();
        String message = record.value();
        String topic = record.topic();

        try {
            postKafkaConsumerHandler.postConsumerEvent(
                    topic,
                    EventType.valueOf(evenType.toUpperCase()),
                    message
            );

        }catch (Exception e){
            log.error("kafka consume fail", e);
        }

    }

}
