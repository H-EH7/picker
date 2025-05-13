package picker.picker_backend.post.component.manger;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.postenum.PostEventType;

@Slf4j
@Component
@AllArgsConstructor
public class PostKafkaProducerManager {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void postSendMessage(String topic, PostEventType eventType, String jsonPostDTO){
        kafkaTemplate.send(topic, eventType.name(), jsonPostDTO);
    }

    public String postConvertToJson(Object postDTO) throws Exception{
        return objectMapper.writeValueAsString(postDTO);
    }

}
