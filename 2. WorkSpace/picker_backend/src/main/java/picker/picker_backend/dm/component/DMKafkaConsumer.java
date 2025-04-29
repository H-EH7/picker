package picker.picker_backend.dm.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import picker.picker_backend.dm.component.session.DMSessionManager;

@Component
@RequiredArgsConstructor
public class DMKafkaConsumer {
    private final DMSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${dm.kafka.topic}", groupId = "${dm.kafka.group-id}")
    public void consume(String message) {
        
    }
}
