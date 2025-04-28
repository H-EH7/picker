package picker.picker_backend.dm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picker.picker_backend.dm.component.DMKafkaProducer;
import picker.picker_backend.dm.component.DMSessionManager;
import picker.picker_backend.dm.model.dto.DMDto;

@Service
@RequiredArgsConstructor
public class DMServiceImpl implements DMService {

    private final DMKafkaProducer kafkaProducer;
    private final DMSessionManager sessionManager;

    @Override
    public void sendDM(DMDto dm) {
        try {
            kafkaProducer.send(dm);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
