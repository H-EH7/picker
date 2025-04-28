package picker.picker_backend.test.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import picker.picker_backend.test.model.dto.TestResponseDto;

public interface TestService {
    /// Mapper 테스트 ======================================
    TestResponseDto getTestById(Long id);

    /// Redis 테스트 ======================================
    void save(String key, String value);
    String get(String key);

    /// Kafka 테스트 ======================================
    void sendMessage(String topic, String message);
    void listen(ConsumerRecord<String, String> record);
}
