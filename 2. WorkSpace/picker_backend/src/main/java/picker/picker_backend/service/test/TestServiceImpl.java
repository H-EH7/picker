package picker.picker_backend.service.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import picker.picker_backend.mapper.TestMapper;
import picker.picker_backend.model.dto.TestResponseDto;
import picker.picker_backend.model.entity.Test;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {

    /// Mapper 테스트 ======================================
    @Autowired
    private final TestMapper testMapper;

    @Override
    public TestResponseDto getTestById(Long id) {
        Test test = testMapper.findById(id);
        if (test == null) return null;

        return new TestResponseDto(
                test.getId(),
                test.getValue()
        );
    }

    /// Redis 테스트 ======================================
    @Autowired
    private final StringRedisTemplate redisTemplate;

    @Override
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /// Kafka 테스트 ======================================
    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        log.debug("메시지 전송: {}", message);
    }

    @Override
    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(ConsumerRecord<String, String> record) {
        String message = record.value();
        if (message != null) {
            log.debug("메시지 수신: {}", record.value());
        }
    }
}
