package picker.picker_backend.post.component.helper;

import org.springframework.stereotype.Component;
import picker.picker_backend.post.model.dto.PostDeleteRequestDTO;
import picker.picker_backend.post.model.dto.PostInsertRequestDTO;
import picker.picker_backend.post.model.dto.PostUpdateRequestDTO;
import picker.picker_backend.post.postenum.PostEventType;
import picker.picker_backend.post.postenum.PostStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class PostRedisHashMapHelper {

    public Map<String, String> createRedisHashMap(Object postDTO, PostEventType eventType){

        Map<String, String> redisHashMap = new HashMap<>();

        redisHashMap.put("eventType", eventType.name());
        redisHashMap.put("status", PostStatus.PROCESSING.name());
        redisHashMap.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        if(postDTO instanceof PostInsertRequestDTO insertDTO){

            redisHashMap.put("userId", insertDTO.getUserId());

        } else if (postDTO instanceof PostUpdateRequestDTO updateDTO) {

            redisHashMap.put("userId", updateDTO.getUserId());

        } else if (postDTO instanceof PostDeleteRequestDTO deleteDTO) {

            redisHashMap.put("userId", deleteDTO.getUserId());

        }

        return redisHashMap;
    }
}
