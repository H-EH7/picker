package picker.picker_backend.post.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import picker.picker_backend.post.config.PostProperties;
import picker.picker_backend.post.model.dto.PostInsertDTO;
import picker.picker_backend.post.model.dto.PostUpdateDTO;
import picker.picker_backend.post.service.PostDLQService;

import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostEventProducer{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PostProperties postProperties;
    private final ObjectMapper objectMapper;
    private final PostDLQService postDLQService;

    public void sendPostEvent(Object postDTO, String eventType){

        String topic = postProperties.getKafka().getTopic();
        String jsonPostDTO = null;

        try{

            jsonPostDTO = objectMapper.writeValueAsString(postDTO);
            kafkaTemplate.send(topic, eventType, jsonPostDTO);

        } catch (Exception e) {
            if(jsonPostDTO != null){
                log.error("Kafka Producer fail", e);
                postDLQService.sendToDLQ(eventType, jsonPostDTO);
            }else{
                log.error("Producer DTO to json Fail", e);
            }
        }
    }

}
