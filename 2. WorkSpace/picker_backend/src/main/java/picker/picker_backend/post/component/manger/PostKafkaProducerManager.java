package picker.picker_backend.post.component.manger;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.helper.PostTopicKeyMapperHelper;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.TopicKey;

@Slf4j
@Component
@AllArgsConstructor
public class PostKafkaProducerManager {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final PostDLQManager postDLQManager;
    private final PostTopicKeyMapperHelper postTopicKeyMapperHelper;

    public void postSendMessage(String topic, EventType eventType, String jsonPostDTO){
        kafkaTemplate.send(topic, eventType.name(), jsonPostDTO);
    }

    public String postConvertToJson(Object postDTO) throws Exception{
        return objectMapper.writeValueAsString(postDTO);
    }

    public void sendEvent(Object dto, EventType eventType, TopicKey topicKey){
        String topic = postTopicKeyMapperHelper.getTopicName(topicKey);
        String jsonDTO = null;
        try{
            jsonDTO = postConvertToJson(dto);

            postSendMessage(topic, eventType, jsonDTO);

        }catch (Exception e){

            if(jsonDTO != null){
                log.error("Kafka Producer fail", e);

                postDLQManager.sendToDLQ(TopicKey.valueOf(topicKey + "DLQ"), eventType, jsonDTO);

            }else{
                log.error("Producer DTO to json Fail", e);
            }
        }
    }

}
