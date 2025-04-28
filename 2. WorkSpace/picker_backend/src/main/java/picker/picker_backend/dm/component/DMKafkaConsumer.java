package picker.picker_backend.dm.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DMKafkaConsumer {
    private final DMSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    private final static String TOPIC = "dm-topic";
    private final static String GROUP_ID = "dm-group";

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void consume(String message) {

    }
}
