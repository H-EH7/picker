package picker.picker_backend.post.component.manger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.helper.PostTopicKeyMapperHelper;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;

import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostDLQManager {

    private final KafkaTemplate<String, String > kafkaTemplate;
    private final PostTopicKeyMapperHelper postTopicKeyMapperHelper;

    public void dlqConsumerEvent(String topic, String message, EventType eventType){

        TopicKey topicKey =postTopicKeyMapperHelper.getTopicKey(topic);

        BiConsumer<EventType,String> topicConsumerHandler = topicHandleMap.get(topicKey);

        if(topicConsumerHandler != null){
            topicConsumerHandler.accept(eventType,message);
        }else{
            log.error("Unknown Event Type : {} ",topicKey);
        }
    }

    private final Map<TopicKey, BiConsumer<EventType,String>> topicHandleMap = Map.of(
            TopicKey.POST, this::postSendToDLQ,
            TopicKey.REPLY, this::replySendToDLQ
    );

    private void postSendToDLQ(EventType eventType, String message){

        sendToDLQ(TopicKey.POSTDLQ, eventType, message);

    }

    private void replySendToDLQ(EventType eventType, String message){
        sendToDLQ(TopicKey.REPLYDLQ, eventType, message);
    }

    public void sendToDLQ(TopicKey topicKey, EventType eventType, String message){

        String topicName = postTopicKeyMapperHelper.getTopicName(topicKey);
        try{

            kafkaTemplate.send(topicName, eventType.name(), message);

        }catch (Exception e){

            log.error("Failed to send to DLQ", e);

        }
    }

}
