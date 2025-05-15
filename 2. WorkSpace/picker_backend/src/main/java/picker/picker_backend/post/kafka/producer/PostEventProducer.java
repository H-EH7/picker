package picker.picker_backend.post.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.PostKafkaProducerManager;
import picker.picker_backend.post.config.PostProperties;
import picker.picker_backend.post.component.manger.PostDLQManager;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventProducer{

    private final PostKafkaProducerManager postKafkaProducerManager;
    private final PostDLQManager postDLQManager;
    private final PostProperties postProperties;


    public void sendPostEvent(Object postDTO, EventType eventType){

        postKafkaProducerManager.sendEvent(postDTO, eventType, TopicKey.POST);

    }

    public void sendReplyEvent(Object replyDTO, EventType eventType){
        postKafkaProducerManager.sendEvent(replyDTO, eventType,TopicKey.REPLY);
    }





}
