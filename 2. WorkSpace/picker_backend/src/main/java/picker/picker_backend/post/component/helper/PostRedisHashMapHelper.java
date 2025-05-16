package picker.picker_backend.post.component.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.component.handler.DBHandler;
import picker.picker_backend.post.model.common.UserIdSupport;
import picker.picker_backend.post.model.dto.PostDeleteRequestDTO;
import picker.picker_backend.post.model.dto.PostInsertRequestDTO;
import picker.picker_backend.post.model.dto.PostUpdateRequestDTO;
import picker.picker_backend.post.postenum.EventType;
import picker.picker_backend.post.postenum.Status;
import picker.picker_backend.post.postenum.TopicKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PostRedisHashMapHelper {

    public Map<String, String> createRedisHashMap(EventType eventType, Object postDTO){
        try {
            Map<String, String> redisHashMap = new HashMap<>();

            redisHashMap.put("eventType", eventType.name());
            redisHashMap.put("status", Status.PROCESSING.name());
            redisHashMap.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            if (postDTO instanceof UserIdSupport userIdSupport) {
                redisHashMap.put("userId", userIdSupport.getUserId());
            }

            return redisHashMap;
        }catch (Exception e){
            log.error("Redis HashMap Failed", e);
            throw e;
        }
    }
}
