package picker.picker_backend.dm.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import picker.picker_backend.dm.model.dto.DMDto;

@Component
@RequiredArgsConstructor
public class DMKafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final static String TOPIC = "dm-topic";
    private final static String GROUP_ID = "dm-group";

    public void send(DMDto dm) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(dm);
        kafkaTemplate.send(TOPIC, json);
    }
}
