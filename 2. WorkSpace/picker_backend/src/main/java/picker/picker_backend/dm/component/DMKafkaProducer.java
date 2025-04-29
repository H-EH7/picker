package picker.picker_backend.dm.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import picker.picker_backend.dm.config.DMProperties;
import picker.picker_backend.dm.model.dto.DMDto;

@Component
@RequiredArgsConstructor
public class DMKafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final DMProperties properties;

    public void send(DMDto dm) {
        try {
            String json = objectMapper.writeValueAsString(dm);
            kafkaTemplate.send(properties.getKafka().getTopic(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
