package picker.picker_backend.post.component.manger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import picker.picker_backend.post.postenum.PostEventType;
import picker.picker_backend.post.postenum.PostStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Component
public class PostRedisStatusManager {

    private final RedisTemplate<String, String> redisTemplate;

    private String getPostRedisTempIdKey(PostEventType eventType, String tempId){
        String postEventType = eventType.name();

        return "post:" + postEventType + ":" + tempId;
    }

    public void setStatus(PostEventType eventType, String tempId, PostStatus postStatus){
        redisTemplate.opsForHash().put(getPostRedisTempIdKey(eventType,tempId), "status", postStatus.name());
    }

    public void setStatusWithTimestamp(PostEventType eventType, String tempId, PostStatus postStatus){
        try{
            redisTemplate.opsForHash().put(getPostRedisTempIdKey(eventType,tempId), "status", postStatus.name());
            redisTemplate.opsForHash().put(
                    getPostRedisTempIdKey(eventType,tempId),
                    "updateAt",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
        }catch (Exception e){
            log.error("Post Redis Status Error ", e);
        }
    }

    public PostStatus getStatus(PostEventType eventType, String tempId){
        String status = (String) redisTemplate.opsForHash().get(getPostRedisTempIdKey(eventType,tempId), "status");
        return status != null ? PostStatus.valueOf(status) : null;
    }

    public void setStatusMap(PostEventType eventType, String tempId, Map<String, String> postStatusMap){
        redisTemplate.opsForHash().putAll(getPostRedisTempIdKey(eventType, tempId), postStatusMap);
    }

    public Map<Object, Object> getPostStatusMap(PostEventType eventType, String tempId){
        return redisTemplate.opsForHash().entries(getPostRedisTempIdKey(eventType,tempId));
    }

    public void setExpireTime(PostEventType eventType, String tempId){
        redisTemplate.expire(getPostRedisTempIdKey(eventType, tempId),24, TimeUnit.HOURS );
    }
}
