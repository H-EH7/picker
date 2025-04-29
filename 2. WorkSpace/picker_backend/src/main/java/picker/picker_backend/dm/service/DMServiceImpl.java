package picker.picker_backend.dm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picker.picker_backend.dm.component.DMKafkaProducer;
import picker.picker_backend.dm.component.session.DMSessionManager;
import picker.picker_backend.dm.model.dto.DMDto;

@Service
@RequiredArgsConstructor
public class DMServiceImpl implements DMService {

    private final DMKafkaProducer kafkaProducer;
    private final DMSessionManager sessionManager;

    @Override
    public void sendDM(DMDto dm) {
        kafkaProducer.send(dm);
    }
}
