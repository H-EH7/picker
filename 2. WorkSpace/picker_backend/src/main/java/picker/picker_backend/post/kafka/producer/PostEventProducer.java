package picker.picker_backend.post.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.manger.PostKafkaProducerManager;
import picker.picker_backend.post.config.PostProperties;
import picker.picker_backend.post.component.manger.PostDLQManager;
import picker.picker_backend.post.postenum.PostEventType;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventProducer{

    private final PostKafkaProducerManager postKafkaProducerManager;
    private final PostDLQManager postDLQManager;
    private final PostProperties postProperties;


    public void sendPostEvent(Object postDTO, PostEventType eventType){

        String topic = postProperties.getKafka().getTopic();
        String jsonPostDTO = null;

        try{

            jsonPostDTO = postKafkaProducerManager.postConvertToJson(postDTO);

            postKafkaProducerManager.postSendMessage(topic, eventType, jsonPostDTO);

        } catch (Exception e) {

            if(jsonPostDTO != null){
                log.error("Kafka Producer fail", e);

                postDLQManager.postSendToDLQ(eventType, jsonPostDTO);

            }else{
                log.error("Producer DTO to json Fail", e);
            }
        }
    }

}
